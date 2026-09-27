package com.example.wordreview.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.wordreview.dto.WordAddRequest;
import com.example.wordreview.entity.Word;

/**
 * 单词业务接口。
 */
public interface WordService {

    /**
     * 分页查询单词。
     *
     * @param current 页码，从 1 开始
     * @param size    每页条数
     * @param keyword 关键词，模糊匹配原文或译文，可为空
     */
    IPage<Word> page(long current, long size, String keyword);

    /**
     * 新增单词，返回带主键的记录。
     */
    Word add(WordAddRequest request);

    /**
     * 删除单词。
     */
    void delete(Long id);
}
