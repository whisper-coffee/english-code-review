package com.example.wordreview.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * 分页返回体，避免把 MyBatis-Plus 的 IPage 内部结构直接暴露给前端。
 *
 * @param <T> 记录类型
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<T> records;

    private long total;

    private long current;

    private long size;

    private long pages;

    public static <T> PageResult<T> of(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setRecords(page.getRecords() == null ? Collections.emptyList() : page.getRecords());
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setPages(page.getPages());
        return result;
    }

    /**
     * 把分页记录转换为另一种类型。
     */
    public static <S, T> PageResult<T> of(IPage<S> page, Function<S, T> converter) {
        PageResult<T> result = new PageResult<>();
        List<S> source = page.getRecords() == null ? Collections.emptyList() : page.getRecords();
        result.setRecords(source.stream().map(converter).toList());
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setPages(page.getPages());
        return result;
    }
}
