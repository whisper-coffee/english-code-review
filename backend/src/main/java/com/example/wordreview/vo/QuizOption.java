package com.example.wordreview.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 选项：key 为 A/B/C/D，translation 为选项展示的译文。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizOption {

    private String key;

    private String translation;
}
