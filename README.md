# English Word Review（英语单词复习网站）

Vue3 + Spring Boot 3 + MyBatis-Plus + MySQL8 的单词背诵小项目。

- **单词列表页**：分页展示已录入单词，支持新增、按关键词搜索、删除，展示提问次数 / 正确次数 / 正确率。
- **背单词页**：随机抽一个英文单词，给出 A/B/C/D 四个译文选项（1 个正确 + 3 个来自其他单词），作答后回写提问次数与正确次数。

## 目录结构

```
english-word-review/
├── backend/            # Spring Boot 3 后端
│   ├── pom.xml
│   └── src/main/java/com/example/wordreview/...
└── frontend/           # Vue3 + Vite 前端
    ├── package.json
    ├── vite.config.js
    └── src/...
```

## 一、数据库准备（MySQL 8）

```sql
CREATE DATABASE word_review DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

然后执行建表脚本 `backend/src/main/resources/db/schema.sql`：

```bash
mysql -u root -p word_review < backend/src/main/resources/db/schema.sql
```

## 二、启动后端

修改 `backend/src/main/resources/application.yml` 中的数据库用户名/密码：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/word_review?useUnicode=true&characterEncoding=utf8mb4&serverTimezone=Asia/Shanghai
    username: root
    password: 你的密码
```

```bash
cd backend
mvn spring-boot:run
# 默认端口 8080
```

## 三、启动前端

```bash
cd frontend
npm install
npm run dev
# 默认 http://localhost:5173，已配置 /api 代理到 8080
```

## 四、接口清单

| Method | 路径 | 说明 |
|---|---|---|
| GET | `/api/words?current=1&size=10&keyword=` | 单词分页列表（keyword 匹配原文/译文） |
| POST | `/api/words` | 新增单词 |
| DELETE | `/api/words/{id}` | 删除单词 |
| GET | `/api/quiz/next` | 随机出一道题（单词 + 4 个选项） |
| POST | `/api/quiz/answer` | 提交答案，回写统计 |

统一返回体：

```json
{ "code": 0, "message": "ok", "data": {} }
```

- `POST /api/words` 请求体：`{ "wordText": "apple", "partOfSpeech": "n.", "translation": "苹果" }`
- `POST /api/quiz/answer` 请求体：`{ "wordId": 1, "selectedTranslation": "苹果" }`
  响应：`{ "correct": true, "correctTranslation": "苹果", "word": { ...最新统计 } }`

## 五、约定与说明

- 单词唯一约束为 `(word_text, part_of_speech)`，重复录入会返回 400。
- 出题使用 `ORDER BY RAND()`，数据量小（个人词库）性能可接受；数据量上万后建议改为随机 ID 采样。
- 答题校验采用「提交所选译文文本 + 服务端比对」的无状态方式，避免正确答案在前端被提前解析。
- 单词总数少于 4 个时无法出题，接口会返回明确错误提示。
