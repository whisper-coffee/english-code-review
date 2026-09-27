package com.example.wordreview.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 单词实体，对应表 words。
 */
@Data
@TableName("words")
public class Word {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 单词原文，如 apple */
    @TableField("word_text")
    private String wordText;

    /** 词性，如 n. / v. / adj. */
    @TableField("part_of_speech")
    private String partOfSpeech;

    /** 译文，如 苹果 */
    @TableField("translation")
    private String translation;

    /** 提问次数 */
    @TableField("ask_count")
    private Integer askCount;

    /** 正确次数 */
    @TableField("correct_count")
    private Integer correctCount;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    /** 正确率（0~1），无提问记录时返回 null，由前端自行展示为 -- */
    @TableField(exist = false)
    private Double accuracy;
}
