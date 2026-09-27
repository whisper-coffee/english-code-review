import axios from 'axios'
import { ElMessage } from 'element-plus'

/**
 * axios 实例：统一走 /api 前缀（开发环境由 Vite 代理到 8080）。
 * 响应拦截器负责拆包与错误提示，业务代码拿到的直接是 data。
 */
const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' }
})

request.interceptors.response.use(
  (response) => {
    const body = response.data
    if (body && body.code === 0) {
      return body.data
    }
    const message = body?.message || '请求失败'
    ElMessage.error(message)
    return Promise.reject(new Error(message))
  },
  (error) => {
    const message = error?.response?.data?.message || error.message || '网络异常，请稍后重试'
    ElMessage.error(message)
    return Promise.reject(new Error(message))
  }
)

export const wordApi = {
  /** 分页列表 */
  page(params) {
    return request.get('/words', { params })
  },
  /** 新增单词 */
  add(data) {
    return request.post('/words', data)
  },
  /** 删除单词 */
  remove(id) {
    return request.delete(`/words/${id}`)
  }
}

export const quizApi = {
  /** 随机出题 */
  next() {
    return request.get('/quiz/next')
  },
  /** 提交答案 */
  answer(data) {
    return request.post('/quiz/answer', data)
  }
}

export default request
