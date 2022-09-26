package com.xuzekun.backend.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常返回
 */
@Getter
@Setter
public class BusinessException extends RuntimeException {

    private Integer showType;

    private Integer errorCode;

    public BusinessException(String message) {
        super(message);
        this.showType = 2;
    }

    public BusinessException(String message, Integer showType) {
        super(message);
        this.showType = showType;
    }

    public BusinessException(String message, Integer showType, Integer errorCode) {
        super(message);
        this.showType = showType;
        this.errorCode = errorCode;
    }
}
