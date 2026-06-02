package com.example.workmanager.substitute.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.substitute.application.dto.request.StaffAvailabilityCreateRequest;
import com.example.workmanager.substitute.application.dto.response.AvailabilityGridResponse;
import com.example.workmanager.substitute.application.dto.response.StaffAvailabilityResponse;
import com.example.workmanager.substitute.application.service.StaffAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/substitute/availability")
@RequiredArgsConstructor
public class StaffAvailabilityController {

    private final StaffAvailabilityService staffAvailabilityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<StaffAvailabilityResponse> createAvailability(
            @AuthenticationPrincipal Long userId,
            @RequestBody StaffAvailabilityCreateRequest request) {
        return ApiResponse.success(staffAvailabilityService.createAvailability(userId, request));
    }

    @DeleteMapping("/{availabilityId}")
    public ApiResponse<Void> deleteAvailability(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long availabilityId) {
        staffAvailabilityService.deleteAvailability(userId, availabilityId);
        return ApiResponse.success(null);
    }

    @GetMapping("/grid")
    public ApiResponse<AvailabilityGridResponse> getAvailabilityGrid(
            @AuthenticationPrincipal Long userId,
            @RequestParam Long storeId) {
        return ApiResponse.success(staffAvailabilityService.getAvailabilityGrid(userId, storeId));
    }
}