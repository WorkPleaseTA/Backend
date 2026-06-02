package com.example.workmanager.substitute.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.substitute.application.dto.request.SubstituteRequestCreateRequest;
import com.example.workmanager.substitute.application.dto.response.IncomingSubstituteResponse;
import com.example.workmanager.substitute.application.dto.response.SubstituteRequestResponse;
import com.example.workmanager.substitute.application.service.SubstituteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/substitute")
@RequiredArgsConstructor
public class SubstituteRequestController {

    private final SubstituteService substituteService;

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<List<SubstituteRequestResponse>> createRequest(
            @AuthenticationPrincipal Long userId,
            @RequestBody SubstituteRequestCreateRequest request) {
        return ApiResponse.success(substituteService.createRequest(userId, request));
    }

    @GetMapping("/requests/my")
    public ApiResponse<List<SubstituteRequestResponse>> getMyRequests(
            @AuthenticationPrincipal Long userId,
            @RequestParam Long storeId) {
        return ApiResponse.success(substituteService.getMyRequests(userId, storeId));
    }

    @GetMapping("/requests/incoming")
    public ApiResponse<List<IncomingSubstituteResponse>> getIncomingRequests(
            @AuthenticationPrincipal Long userId,
            @RequestParam Long storeId) {
        return ApiResponse.success(substituteService.getIncomingRequests(userId, storeId));
    }

    @PostMapping("/candidates/{candidateId}/accept")
    public ApiResponse<Void> acceptRequest(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long candidateId) {
        substituteService.acceptRequest(userId, candidateId);
        return ApiResponse.success(null);
    }

    @PostMapping("/candidates/{candidateId}/decline")
    public ApiResponse<Void> declineRequest(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long candidateId) {
        substituteService.declineRequest(userId, candidateId);
        return ApiResponse.success(null);
    }
}