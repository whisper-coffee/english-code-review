package com.example.wordreview.controller;

import com.example.wordreview.common.PageResult;
import com.example.wordreview.common.R;
import com.example.wordreview.dto.WordAddRequest;
import com.example.wordreview.entity.Word;
import com.example.wordreview.service.WordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 单词接口：分页列表 / 新增 / 删除。
 */
@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @GetMapping
    public R<PageResult<Word>> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String keyword) {
        return R.ok(PageResult.of(wordService.page(current, size, keyword)));
    }

    @PostMapping
    public R<Word> add(@Valid @RequestBody WordAddRequest request) {
        return R.ok(wordService.add(request));
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        wordService.delete(id);
        return R.ok();
    }
}
