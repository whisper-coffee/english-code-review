package com.example.wordreview.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 新增单词请求体。
 */
@Data
public class WordAddRequest {

    @NotBlank(message = "单词原文不能为空")
    @Size(max = 128, message = "单词原文长度不能超过 128")
    private String wordText;

    @Size(max = 32, message = "词性长度不能超过 32")
    private String partOfSpeech;

    @NotBlank(message = "译文不能为空")
    @Size(max = 255, message = "译文长度不能超过 255")
    private String translation;
}
