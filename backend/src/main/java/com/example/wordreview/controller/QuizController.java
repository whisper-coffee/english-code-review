package com.example.wordreview.controller;

import com.example.wordreview.common.R;
import com.example.wordreview.dto.QuizAnswerRequest;
import com.example.wordreview.service.QuizService;
import com.example.wordreview.vo.QuizResultVO;
import com.example.wordreview.vo.QuizVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 背单词接口：随机出题 / 提交答案。
 */
@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/next")
    public R<QuizVO> next() {
        return R.ok(quizService.nextQuiz());
    }

    @PostMapping("/answer")
    public R<QuizResultVO> answer(@Valid @RequestBody QuizAnswerRequest request) {
        return R.ok(quizService.answer(request));
    }
}
