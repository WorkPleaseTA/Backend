package com.example.workmanager.global.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode errorcode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorcode = errorCode;
    }

    public BaseException(ErrorCode errorcode, String message) {
        super(errorcode.getMessage() + " -> " + message);
        this.errorcode = errorcode;
    }
}
