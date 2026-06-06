package com.example.workmanager.schedule.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.schedule.application.dto.response.TodayScheduleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "MySchedule", description = "내 근무 스케줄 조회")
public interface MyScheduleControllerDocs {

    @Operation(summary = "내 근무 스케줄 조회",
            description = "date 파라미터 없으면 오늘 기준. 대타 수락 스케줄 우선(isSubstitute=true), 없으면 고정 스케줄")
    ApiResponse<List<TodayScheduleResponse>> getTodaySchedule(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "조회 날짜 (yyyy-MM-dd), 생략 시 오늘") LocalDate date);
}