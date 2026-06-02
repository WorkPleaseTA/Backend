package com.example.workmanager.substitute.domain.exception;

import com.example.workmanager.global.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SubstituteErrorCode implements ErrorCode {

    INVALID_TIME_RANGE(HttpStatus.BAD_REQUEST, "SUB000", "종료 시간은 시작 시간 이후여야 합니다."),
    AVAILABILITY_NOT_FOUND(HttpStatus.NOT_FOUND, "SUB001", "대타 가능 시간을 찾을 수 없습니다."),
    OVERLAPS_WITH_FIXED_SCHEDULE(HttpStatus.CONFLICT, "SUB002", "고정 스케줄과 시간이 겹칩니다."),
    OVERLAPS_WITH_EXISTING_AVAILABILITY(HttpStatus.CONFLICT, "SUB003", "기존 대타 가능 시간과 겹칩니다."),
    NOT_YOUR_AVAILABILITY(HttpStatus.FORBIDDEN, "SUB004", "본인의 대타 가능 시간이 아닙니다."),
    SUBSTITUTE_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "SUB005", "대타 요청을 찾을 수 없습니다."),
    CANDIDATE_NOT_FOUND(HttpStatus.NOT_FOUND, "SUB006", "대타 후보를 찾을 수 없습니다."),
    NOT_YOUR_CANDIDATE(HttpStatus.FORBIDDEN, "SUB007", "본인에게 온 대타 요청이 아닙니다."),
    ALREADY_PROCESSED(HttpStatus.CONFLICT, "SUB008", "이미 처리된 요청입니다."),
    NOT_MEMBER_OF_STORE(HttpStatus.FORBIDDEN, "SUB009", "해당 매장의 직원이 아닙니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}