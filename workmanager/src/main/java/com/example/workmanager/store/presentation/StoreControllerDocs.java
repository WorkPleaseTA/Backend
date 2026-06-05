package com.example.workmanager.store.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.store.application.dto.request.StoreCreateRequest;
import com.example.workmanager.store.application.dto.request.StoreJoinRequest;
import com.example.workmanager.store.application.dto.response.StoreCreateResponse;
import com.example.workmanager.store.application.dto.response.StoreDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Store", description = "가게 등록 / 합류 / 조회")
public interface StoreControllerDocs {

    @Operation(summary = "가게 등록 (OWNER 전용)", description = "가게 생성 후 초대코드 발급. OWNER만 사용 가능")
    ApiResponse<StoreCreateResponse> createStore(
            @Parameter(hidden = true) Long userId,
            StoreCreateRequest request);

    @Operation(summary = "가게 합류 (STAFF 전용)", description = "초대코드 입력으로 가게에 합류. store_members ACTIVE 등록")
    ApiResponse<Void> joinStore(
            @Parameter(hidden = true) Long userId,
            StoreJoinRequest request);

    @Operation(summary = "내 가게 목록 조회", description = "소속된 모든 가게 정보 + 직원 목록(근무 요일 포함) 반환")
    ApiResponse<List<StoreDetailResponse>> getMyStores(
            @Parameter(hidden = true) Long userId);
}