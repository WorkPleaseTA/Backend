package com.example.workmanager.substitute.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.substitute.application.dto.response.SubstituteCandidateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.LocalTime;

@Tag(name = "SubstituteCandidate", description = "AI 대타 후보 조회")
public interface SubstituteCandidateControllerDocs {

    @Operation(summary = "AI 대타 후보 조회",
            description = "StaffAvailability 기반 후보를 AI가 적합도 순으로 정렬해 반환. DB 미기록 (순수 조회)")
    ApiResponse<SubstituteCandidateResponse> findCandidates(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "가게 ID", required = true) Long storeId,
            @Parameter(description = "대타 필요 날짜 (yyyy-MM-dd)", example = "2026-06-10", required = true) LocalDate date,
            @Parameter(description = "시작 시간 (HH:mm:ss)", example = "09:00:00", required = true) LocalTime startTime,
            @Parameter(description = "종료 시간 (HH:mm:ss)", example = "18:00:00", required = true) LocalTime endTime);
}