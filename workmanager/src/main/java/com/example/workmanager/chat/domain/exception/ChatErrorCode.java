package com.example.workmanager.chat.domain.exception;

import com.example.workmanager.global.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {

    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT001", "채팅방을 찾을 수 없습니다."),
    NOT_ROOM_MEMBER(HttpStatus.FORBIDDEN, "CHAT002", "채팅방 참여자가 아닙니다."),
    GROUP_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT003", "단체 채팅방을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}