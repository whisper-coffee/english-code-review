package com.example.wordreview.common;

import lombok.Getter;

/**
 * 业务异常：由 GlobalExceptionHandler 捕获并以 400 + 提示语返回给前端。
 */
@Getter
public class BizException extends RuntimeException {

    private final int code;

    public BizException(String message) {
        super(message);
        this.code = 400;
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }
}
