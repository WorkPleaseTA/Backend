package com.example.workmanager.substitute.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.substitute.application.dto.request.SubstituteRequestCreateRequest;
import com.example.workmanager.substitute.application.dto.response.DailySubstituteChangeResponse;
import com.example.workmanager.substitute.application.dto.response.IncomingSubstituteResponse;
import com.example.workmanager.substitute.application.dto.response.SubstituteRequestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "SubstituteRequest", description = "대타 요청 생성 / 조회 / 수락 / 거절")
public interface SubstituteRequestControllerDocs {

    @Operation(summary = "대타 요청 생성",
            description = "selectedStoreMemberIds로 후보 지정. GET /substitute/candidates 응답에서 선택한 ID 사용")
    ApiResponse<List<SubstituteRequestResponse>> createRequest(
            @Parameter(hidden = true) Long userId,
            SubstituteRequestCreateRequest request);

    @Operation(summary = "내 대타 요청 목록 조회", description = "내가 보낸 대타 요청 현황 (후보별 카드)")
    ApiResponse<List<SubstituteRequestResponse>> getMyRequests(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "가게 ID") Long storeId);

    @Operation(summary = "들어온 대타 요청 조회", description = "나에게 들어온 대타 요청 알림 목록")
    ApiResponse<List<IncomingSubstituteResponse>> getIncomingRequests(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "가게 ID") Long storeId);

    @Operation(summary = "당일 대타 변동 조회 (OWNER 전용)",
            description = "특정 날짜에 수락된 대타 목록 반환. 결근자(absenteeName)와 대체자(substituteName) 포함")
    ApiResponse<List<DailySubstituteChangeResponse>> getDailySubstituteChanges(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "가게 ID") Long storeId,
            @Parameter(description = "조회 날짜 (yyyy-MM-dd)") LocalDate date);

    @Operation(summary = "대타 수락",
            description = "수락 시 work_schedules 자동 생성. 나머지 WAITING 후보 자동 DECLINED 처리")
    ApiResponse<Void> acceptRequest(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "대타 후보 ID") Long candidateId);

    @Operation(summary = "대타 거절")
    ApiResponse<Void> declineRequest(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "대타 후보 ID") Long candidateId);
}