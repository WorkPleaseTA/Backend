package com.example.workmanager.ai.domain.exception;

import com.example.workmanager.global.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AiErrorCode implements ErrorCode {

    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "AI001", "채팅방을 찾을 수 없습니다."),
    CHAT_ROOM_ACCESS_DENIED(HttpStatus.FORBIDDEN, "AI002", "해당 채팅방에 접근 권한이 없습니다."),
    AI_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI003", "AI 요청 처리에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}