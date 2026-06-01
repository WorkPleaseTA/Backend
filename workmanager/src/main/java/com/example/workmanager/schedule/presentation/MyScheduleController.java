package com.example.workmanager.schedule.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.schedule.application.dto.response.TodayScheduleResponse;
import com.example.workmanager.schedule.application.service.FixedScheduleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule/my")
@RequiredArgsConstructor
public class MyScheduleController {

    private final FixedScheduleService fixedScheduleService;

    @GetMapping("/today")
    public ApiResponse<List<TodayScheduleResponse>> getTodaySchedule(
            @AuthenticationPrincipal Long userId) {
        return ApiResponse.success(fixedScheduleService.getTodaySchedule(userId));
    }
}