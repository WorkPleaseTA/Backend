package com.example.workmanager.substitute.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.substitute.application.dto.request.StaffAvailabilityCreateRequest;
import com.example.workmanager.substitute.application.dto.response.AvailabilityGridResponse;
import com.example.workmanager.substitute.application.dto.response.StaffAvailabilityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "StaffAvailability", description = "대타 가능 시간 관리")
public interface StaffAvailabilityControllerDocs {

    @Operation(summary = "대타 가능 시간 등록",
            description = "요일별 대타 가능 시간 등록. 고정 스케줄 또는 기존 가용 시간과 겹치면 409")
    ApiResponse<StaffAvailabilityResponse> createAvailability(
            @Parameter(hidden = true) Long userId,
            StaffAvailabilityCreateRequest request);

    @Operation(summary = "대타 가능 시간 삭제")
    ApiResponse<Void> deleteAvailability(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "가용 시간 ID") Long availabilityId);

    @Operation(summary = "대타 가능 시간 그리드 조회",
            description = "매장 전 직원의 고정 스케줄 + 대타 가능 시간 통합 조회. 그리드 UI용")
    ApiResponse<AvailabilityGridResponse> getAvailabilityGrid(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "가게 ID") Long storeId);
}