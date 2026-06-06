package com.example.workmanager.schedule.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.schedule.application.dto.response.TodayScheduleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "MySchedule", description = "내 근무 스케줄 조회")
public interface MyScheduleControllerDocs {

    @Operation(summary = "오늘 내 근무 스케줄 조회",
            description = "소속된 모든 가게의 오늘 스케줄 반환. 대타 수락 스케줄 우선(isSubstitute=true), 없으면 고정 스케줄")
    ApiResponse<List<TodayScheduleResponse>> getTodaySchedule(
            @Parameter(hidden = true) Long userId);
}