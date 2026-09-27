package com.example.wordreview.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wordreview.entity.Word;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 单词 Mapper。随机取词的 SQL 见 resources/mapper/WordMapper.xml。
 */
public interface WordMapper extends BaseMapper<Word> {

    /**
     * 随机取一个单词。
     */
    Word selectRandomOne();

    /**
     * 随机取若干个「非指定 id」的单词，用于生成干扰项。
     *
     * @param excludeId 需要排除的单词 id（正确答案所在单词）
     * @param limit     取多少条候选
     */
    List<Word> selectRandomExcept(@Param("excludeId") Long excludeId, @Param("limit") int limit);
}
