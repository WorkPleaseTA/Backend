package com.example.workmanager.store.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.store.application.dto.request.StoreCreateRequest;
import com.example.workmanager.store.application.dto.request.StoreJoinRequest;
import com.example.workmanager.store.application.dto.response.StoreCreateResponse;
import com.example.workmanager.store.application.dto.response.StoreDetailResponse;
import com.example.workmanager.store.application.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<StoreCreateResponse> createStore(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid StoreCreateRequest request) {
        return ApiResponse.success(storeService.createStore(userId, request));
    }

    @PostMapping("/join")
    public ApiResponse<Void> joinStore(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid StoreJoinRequest request) {
        storeService.joinStore(userId, request);
        return ApiResponse.success("가게에 합류했습니다.");
    }

    @GetMapping("/me")
    public ApiResponse<List<StoreDetailResponse>> getMyStores(
            @AuthenticationPrincipal Long userId) {
        return ApiResponse.success(storeService.getMyStores(userId));
    }
}