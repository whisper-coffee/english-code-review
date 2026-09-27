package com.example.wordreview.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wordreview.common.BizException;
import com.example.wordreview.dto.WordAddRequest;
import com.example.wordreview.entity.Word;
import com.example.wordreview.mapper.WordMapper;
import com.example.wordreview.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 单词业务实现。
 */
@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {

    private final WordMapper wordMapper;

    @Override
    public IPage<Word> page(long current, long size, String keyword) {
        // 防御性处理：避免前端传入 0 或负数导致 SQL 报错
        long pageNo = Math.max(current, 1);
        long pageSize = Math.min(Math.max(size, 1), 100);

        LambdaQueryWrapper<Word> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            String kw = keyword.trim();
            wrapper.like(Word::getWordText, kw)
                    .or()
                    .like(Word::getTranslation, kw);
        }
        wrapper.orderByDesc(Word::getId);

        IPage<Word> page = wordMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        List<Word> records = page.getRecords();
        if (records != null) {
            records.forEach(this::fillAccuracy);
        }
        return page;
    }

    @Override
    public Word add(WordAddRequest request) {
        String wordText = request.getWordText().trim();
        String translation = request.getTranslation().trim();
        String partOfSpeech = request.getPartOfSpeech() == null ? "" : request.getPartOfSpeech().trim();

        // 唯一键 (word_text, part_of_speech) 冲突时给出友好提示，而不是抛 SQL 异常
        Long exists = wordMapper.selectCount(new LambdaQueryWrapper<Word>()
                .eq(Word::getWordText, wordText)
                .eq(Word::getPartOfSpeech, partOfSpeech));
        if (exists != null && exists > 0) {
            throw new BizException("单词 " + wordText + "（" + partOfSpeech + "）已存在，请勿重复添加");
        }

        Word word = new Word();
        word.setWordText(wordText);
        word.setPartOfSpeech(partOfSpeech);
        word.setTranslation(translation);
        word.setAskCount(0);
        word.setCorrectCount(0);
        wordMapper.insert(word);
        return word;
    }

    @Override
    public void delete(Long id) {
        if (id == null || wordMapper.selectById(id) == null) {
            throw new BizException("单词不存在或已被删除");
        }
        wordMapper.deleteById(id);
    }

    private void fillAccuracy(Word word) {
        Integer ask = word.getAskCount();
        Integer correct = word.getCorrectCount();
        if (ask != null && ask > 0 && correct != null) {
            word.setAccuracy(correct * 1.0 / ask);
        }
    }
}
