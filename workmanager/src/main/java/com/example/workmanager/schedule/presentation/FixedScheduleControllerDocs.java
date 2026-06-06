package com.example.workmanager.schedule.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.schedule.application.dto.request.FixedScheduleCreateRequest;
import com.example.workmanager.schedule.application.dto.request.FixedScheduleUpdateRequest;
import com.example.workmanager.schedule.application.dto.response.FixedScheduleEditResponse;
import com.example.workmanager.schedule.application.dto.response.FixedScheduleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.DayOfWeek;
import java.util.List;

@Tag(name = "FixedSchedule", description = "고정 스케줄 CRUD (OWNER 전용)")
public interface FixedScheduleControllerDocs {

    @Operation(summary = "고정 스케줄 등록", description = "요일별 직원 고정 스케줄 등록. 동일 직원+요일 중복 시 409")
    ApiResponse<Void> createFixedSchedules(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "가게 ID") Long storeId,
            FixedScheduleCreateRequest request);

    @Operation(summary = "고정 스케줄 조회", description = "dayOfWeek 미입력 시 전 요일 조회. 달력 표시용")
    ApiResponse<List<FixedScheduleResponse>> getFixedSchedules(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "가게 ID") Long storeId,
            @Parameter(description = "요일 (MONDAY~SUNDAY). 미입력 시 전체") DayOfWeek dayOfWeek);

    @Operation(summary = "고정 스케줄 편집 화면용 조회", description = "전체 STAFF + 스케줄 등록 여부 함께 반환. 편집 UI 전용")
    ApiResponse<List<FixedScheduleEditResponse>> getFixedScheduleEditView(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "가게 ID") Long storeId,
            @Parameter(description = "요일 (MONDAY~SUNDAY)", required = true) DayOfWeek dayOfWeek);

    @Operation(summary = "고정 스케줄 저장 (upsert)", description = "스케줄이 있으면 수정, 없으면 생성")
    ApiResponse<Void> upsertFixedSchedules(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "가게 ID") Long storeId,
            FixedScheduleUpdateRequest request);

    @Operation(summary = "고정 스케줄 단건 삭제")
    ApiResponse<Void> deleteFixedSchedule(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "가게 ID") Long storeId,
            @Parameter(description = "스케줄 ID") Long scheduleId);
}