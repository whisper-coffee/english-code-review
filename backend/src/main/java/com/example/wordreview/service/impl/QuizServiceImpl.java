package com.example.wordreview.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.wordreview.common.BizException;
import com.example.wordreview.dto.QuizAnswerRequest;
import com.example.wordreview.entity.Word;
import com.example.wordreview.mapper.WordMapper;
import com.example.wordreview.service.QuizService;
import com.example.wordreview.vo.QuizOption;
import com.example.wordreview.vo.QuizResultVO;
import com.example.wordreview.vo.QuizVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 背单词业务实现：随机抽题 + 选项组装 + 答题统计。
 */
@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    /** 选项总数（1 个正确 + 3 个干扰） */
    private static final int OPTION_SIZE = 4;

    /** 干扰项候选池大小，用于去重后仍凑不满 3 个时的兜底 */
    private static final int DISTRACTOR_POOL_SIZE = 20;

    private final WordMapper wordMapper;

    @Override
    public QuizVO nextQuiz() {
        long total = wordMapper.selectCount(null);
        if (total < OPTION_SIZE) {
            throw new BizException("单词数量至少需要 " + OPTION_SIZE + " 个才能开始背诵，请先添加单词");
        }

        Word target = wordMapper.selectRandomOne();
        if (target == null) {
            throw new BizException("暂无可用单词");
        }

        List<String> distractors = pickDistractors(target);
        if (distractors.size() < OPTION_SIZE - 1) {
            throw new BizException("可用干扰项不足，请先录入更多「译文不同」的单词");
        }

        List<String> translations = new ArrayList<>();
        translations.add(target.getTranslation());
        translations.addAll(distractors);
        Collections.shuffle(translations);

        List<QuizOption> options = new ArrayList<>(OPTION_SIZE);
        String[] keys = {"A", "B", "C", "D"};
        for (int i = 0; i < translations.size(); i++) {
            options.add(new QuizOption(keys[i], translations.get(i)));
        }

        QuizVO vo = new QuizVO();
        vo.setWordId(target.getId());
        vo.setWordText(target.getWordText());
        vo.setPartOfSpeech(target.getPartOfSpeech());
        vo.setOptions(options);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QuizResultVO answer(QuizAnswerRequest request) {
        Word word = wordMapper.selectById(request.getWordId());
        if (word == null) {
            throw new BizException("单词不存在或已被删除");
        }

        String selected = request.getSelectedTranslation() == null
                ? ""
                : request.getSelectedTranslation().trim();
        String correctTranslation = word.getTranslation() == null ? "" : word.getTranslation().trim();
        boolean correct = correctTranslation.equalsIgnoreCase(selected);

        // 用 SQL 自增更新，避免并发下「读-改-写」丢更新
        String incrementSql = correct
                ? "ask_count = ask_count + 1, correct_count = correct_count + 1"
                : "ask_count = ask_count + 1";
        wordMapper.update(null, new LambdaUpdateWrapper<Word>()
                .eq(Word::getId, word.getId())
                .setSql(incrementSql));

        Word updated = wordMapper.selectById(word.getId());

        QuizResultVO result = new QuizResultVO();
        result.setCorrect(correct);
        result.setCorrectTranslation(correctTranslation);
        result.setSelectedTranslation(selected);
        result.setWord(updated);
        return result;
    }

    /**
     * 选取 3 个与正确答案「译文不同」且互不重复的干扰项。
     */
    private List<String> pickDistractors(Word target) {
        String targetTranslation = target.getTranslation() == null ? "" : target.getTranslation().trim();
        Set<String> distinct = new LinkedHashSet<>();

        List<Word> pool = wordMapper.selectRandomExcept(target.getId(), DISTRACTOR_POOL_SIZE);
        for (Word candidate : pool) {
            String translation = candidate.getTranslation() == null ? "" : candidate.getTranslation().trim();
            if (translation.isEmpty() || translation.equalsIgnoreCase(targetTranslation)) {
                continue;
            }
            distinct.add(translation);
            if (distinct.size() >= OPTION_SIZE - 1) {
                break;
            }
        }
        return new ArrayList<>(distinct);
    }
}
