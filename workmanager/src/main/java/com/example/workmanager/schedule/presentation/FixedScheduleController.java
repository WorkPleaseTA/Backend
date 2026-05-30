package com.example.workmanager.schedule.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.schedule.application.dto.request.FixedScheduleCreateRequest;
import com.example.workmanager.schedule.application.dto.request.FixedScheduleUpdateRequest;
import com.example.workmanager.schedule.application.dto.response.FixedScheduleEditResponse;
import com.example.workmanager.schedule.application.dto.response.FixedScheduleResponse;
import com.example.workmanager.schedule.application.service.FixedScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/stores/{storeId}/fixed-schedules")
@RequiredArgsConstructor
public class FixedScheduleController {

    private final FixedScheduleService fixedScheduleService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<Void> createFixedSchedules(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long storeId,
            @RequestBody @Valid FixedScheduleCreateRequest request) {
        fixedScheduleService.createFixedSchedules(userId, storeId, request);
        return ApiResponse.success("고정 스케줄이 등록되었습니다.");
    }

    @GetMapping
    public ApiResponse<List<FixedScheduleResponse>> getFixedSchedules(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long storeId,
            @RequestParam(required = false) DayOfWeek dayOfWeek) {
        return ApiResponse.success(fixedScheduleService.getFixedSchedules(userId, storeId, dayOfWeek));
    }

    @GetMapping("/edit-view")
    public ApiResponse<List<FixedScheduleEditResponse>> getFixedScheduleEditView(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long storeId,
            @RequestParam("dayOfWeek") DayOfWeek dayOfWeek) {
        return ApiResponse.success(fixedScheduleService.getFixedScheduleEditView(userId, storeId, dayOfWeek));
    }

    @PutMapping
    public ApiResponse<Void> upsertFixedSchedules(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long storeId,
            @RequestBody @Valid FixedScheduleUpdateRequest request) {
        fixedScheduleService.upsertFixedSchedules(userId, storeId, request);
        return ApiResponse.success("고정 스케줄이 저장되었습니다.");
    }

    @DeleteMapping("/{scheduleId}")
    public ApiResponse<Void> deleteFixedSchedule(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long storeId,
            @PathVariable Long scheduleId) {
        fixedScheduleService.deleteFixedSchedule(userId, storeId, scheduleId);
        return ApiResponse.success("고정 스케줄이 삭제되었습니다.");
    }
}