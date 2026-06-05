package com.example.workmanager.ai.presentation;

import com.example.workmanager.ai.application.dto.request.AiTodoExtractRequest;
import com.example.workmanager.ai.application.dto.response.AiTodoExtractResponse;
import com.example.workmanager.ai.application.service.AiTodoService;
import com.example.workmanager.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiTodoService aiTodoService;

    @PostMapping("/todos/extract")
    public ApiResponse<AiTodoExtractResponse> extractTodos(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid AiTodoExtractRequest request) {
        return ApiResponse.success(aiTodoService.extractTodos(userId, request.getRoomId()));
    }
}