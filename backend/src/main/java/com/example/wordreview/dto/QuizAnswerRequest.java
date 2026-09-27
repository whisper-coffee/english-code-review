package com.example.wordreview.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 提交答案请求体。
 * 只提交「所选译文文本」，由服务端与正解比对，避免正确答案在响应中被提前泄露。
 */
@Data
public class QuizAnswerRequest {

    @NotNull(message = "单词 id 不能为空")
    private Long wordId;

    @NotBlank(message = "请选择答案")
    private String selectedTranslation;
}
