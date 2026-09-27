package com.example.wordreview.vo;

import lombok.Data;

import java.util.List;

/**
 * 一道题：题干（英文单词）+ 四个译文选项。
 */
@Data
public class QuizVO {

    private Long wordId;

    private String wordText;

    private String partOfSpeech;

    /** 固定 4 个选项，乱序，有且仅有一个为正确答案 */
    private List<QuizOption> options;
}
