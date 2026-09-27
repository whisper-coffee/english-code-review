package com.example.wordreview.service;

import com.example.wordreview.dto.QuizAnswerRequest;
import com.example.wordreview.vo.QuizResultVO;
import com.example.wordreview.vo.QuizVO;

/**
 * 背单词（出题 / 答题）业务接口。
 */
public interface QuizService {

    /**
     * 随机生成一道题。
     */
    QuizVO nextQuiz();

    /**
     * 提交答案并累计提问次数 / 正确次数。
     */
    QuizResultVO answer(QuizAnswerRequest request);
}
