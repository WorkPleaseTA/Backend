package com.example.workmanager.store.domain.exception;

import com.example.workmanager.global.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements ErrorCode {

    ACCESS_DENIED(HttpStatus.FORBIDDEN, "S001", "접근 권한이 없습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "S002", "가게를 찾을 수 없습니다."),
    INVALID_INVITE_CODE(HttpStatus.BAD_REQUEST, "S003", "유효하지 않은 초대 코드입니다."),
    ALREADY_JOINED(HttpStatus.CONFLICT, "S004", "이미 소속된 가게입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}