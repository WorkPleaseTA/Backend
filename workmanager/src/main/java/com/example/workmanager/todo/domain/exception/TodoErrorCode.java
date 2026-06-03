package com.example.workmanager.todo.domain.exception;

import com.example.workmanager.global.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TodoErrorCode implements ErrorCode {

    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO001", "TODO를 찾을 수 없습니다."),
    NOT_MEMBER_OF_STORE(HttpStatus.FORBIDDEN, "TODO002", "해당 매장의 직원이 아닙니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}