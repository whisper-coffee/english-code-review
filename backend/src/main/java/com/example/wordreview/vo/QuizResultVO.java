package com.example.wordreview.vo;

import com.example.wordreview.entity.Word;
import lombok.Data;

/**
 * 答题结果。
 */
@Data
public class QuizResultVO {

    /** 是否答对 */
    private Boolean correct;

    /** 正确答案译文 */
    private String correctTranslation;

    /** 用户所选译文 */
    private String selectedTranslation;

    /** 更新后的单词（含最新提问次数/正确次数） */
    private Word word;
}
