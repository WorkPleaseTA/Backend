package com.example.workmanager.schedule.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.schedule.application.dto.response.DailyScheduleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "StoreSchedule", description = "매장 전직원 캘린더 조회")
public interface StoreScheduleControllerDocs {

    @Operation(summary = "매장 일별 스케줄 조회",
            description = "특정 날짜의 매장 전 직원 스케줄 반환. 우선순위: 대타(work_schedule) > 고정(fixed_schedule). 대타 요청자는 제외")
    ApiResponse<List<DailyScheduleResponse>> getDailySchedule(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "가게 ID") Long storeId,
            @Parameter(description = "조회 날짜 (yyyy-MM-dd)", example = "2026-06-10") LocalDate date);
}