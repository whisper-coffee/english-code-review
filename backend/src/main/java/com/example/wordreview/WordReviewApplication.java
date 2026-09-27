package com.example.wordreview;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 英语单词复习后端启动类。
 */
@MapperScan("com.example.wordreview.mapper")
@SpringBootApplication
public class WordReviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordReviewApplication.class, args);
    }
}
