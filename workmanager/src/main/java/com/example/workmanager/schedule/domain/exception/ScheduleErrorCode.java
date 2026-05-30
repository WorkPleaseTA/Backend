package com.example.workmanager.schedule.domain.exception;

import com.example.workmanager.global.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScheduleErrorCode implements ErrorCode {

    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SC001", "스케줄을 찾을 수 없습니다."),
    DUPLICATE_FIXED_SCHEDULE(HttpStatus.CONFLICT, "SC002", "해당 직원의 해당 요일에 이미 등록된 고정 스케줄이 있습니다."),
    INVALID_TIME_RANGE(HttpStatus.BAD_REQUEST, "SC003", "종료 시간은 시작 시간 이후여야 합니다."),
    STORE_MEMBER_NOT_IN_STORE(HttpStatus.BAD_REQUEST, "SC004", "해당 직원은 이 매장 소속이 아닙니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
