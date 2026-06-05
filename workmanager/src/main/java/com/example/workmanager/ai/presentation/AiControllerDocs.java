package com.example.workmanager.ai.presentation;

import com.example.workmanager.ai.application.dto.request.AiTodoExtractRequest;
import com.example.workmanager.ai.application.dto.response.AiTodoExtractResponse;
import com.example.workmanager.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "AI", description = "AI 기능 (채팅 → TODO 추출)")
public interface AiControllerDocs {

    @Operation(summary = "채팅방 TODO 추출",
            description = "채팅방 최근 50개 메시지를 GPT(gpt-4o-mini)로 분석해 할 일 후보 반환. 저장은 POST /todos로 별도 요청")
    ApiResponse<AiTodoExtractResponse> extractTodos(
            @Parameter(hidden = true) Long userId,
            AiTodoExtractRequest request);
}