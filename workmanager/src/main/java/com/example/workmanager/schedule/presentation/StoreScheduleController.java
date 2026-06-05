package com.example.workmanager.schedule.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.schedule.application.dto.response.DailyScheduleResponse;
import com.example.workmanager.schedule.application.service.FixedScheduleService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores/{storeId}/schedules")
@RequiredArgsConstructor
public class StoreScheduleController {

    private final FixedScheduleService fixedScheduleService;

    @GetMapping
    public ApiResponse<List<DailyScheduleResponse>> getDailySchedule(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long storeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.success(fixedScheduleService.getDailySchedule(userId, storeId, date));
    }
}