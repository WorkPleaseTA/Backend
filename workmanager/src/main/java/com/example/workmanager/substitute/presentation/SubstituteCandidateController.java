package com.example.workmanager.substitute.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.substitute.application.dto.response.SubstituteCandidateResponse;
import com.example.workmanager.substitute.application.service.StaffAvailabilityService;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/substitute/candidates")
@RequiredArgsConstructor
public class SubstituteCandidateController implements SubstituteCandidateControllerDocs {

    private final StaffAvailabilityService staffAvailabilityService;

    @GetMapping
    public ApiResponse<SubstituteCandidateResponse> findCandidates(
            @AuthenticationPrincipal Long userId,
            @RequestParam Long storeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
        return ApiResponse.success(staffAvailabilityService.findCandidates(userId, storeId, date, startTime, endTime));
    }
}