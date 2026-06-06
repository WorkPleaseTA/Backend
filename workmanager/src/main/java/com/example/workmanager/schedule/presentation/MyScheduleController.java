package com.example.workmanager.schedule.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.schedule.application.dto.response.TodayScheduleResponse;
import com.example.workmanager.schedule.application.service.FixedScheduleService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule/my")
@RequiredArgsConstructor
public class MyScheduleController implements MyScheduleControllerDocs {

    private final FixedScheduleService fixedScheduleService;

    @GetMapping("/today")
    public ApiResponse<List<TodayScheduleResponse>> getTodaySchedule(
            @AuthenticationPrincipal Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.success(fixedScheduleService.getTodaySchedule(userId, date));
    }
}