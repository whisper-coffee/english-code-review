-- 英语单词复习：建表脚本（MySQL 8）
CREATE DATABASE IF NOT EXISTS word_review DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE word_review;

DROP TABLE IF EXISTS words;

CREATE TABLE words (
    id             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    word_text      VARCHAR(128) NOT NULL COMMENT '单词原文，如 apple',
    part_of_speech VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '词性，如 n. v. adj.',
    translation    VARCHAR(255) NOT NULL COMMENT '译文，如 苹果',
    ask_count      INT          NOT NULL DEFAULT 0 COMMENT '提问次数',
    correct_count  INT          NOT NULL DEFAULT 0 COMMENT '正确次数',
    created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_word_pos (word_text, part_of_speech),
    KEY idx_word_text (word_text)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '单词表';

-- 可选示例数据
INSERT INTO words (word_text, part_of_speech, translation) VALUES
    ('apple', 'n.', '苹果'),
    ('banana', 'n.', '香蕉'),
    ('orange', 'n.', '橙子'),
    ('grape', 'n.', '葡萄'),
    ('pear', 'n.', '梨');
