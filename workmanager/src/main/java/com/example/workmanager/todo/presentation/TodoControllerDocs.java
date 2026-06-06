package com.example.workmanager.todo.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.todo.application.dto.request.TodoCreateRequest;
import com.example.workmanager.todo.application.dto.response.TodoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Todo", description = "매장 공유 TODO 관리")
public interface TodoControllerDocs {

    @Operation(summary = "TODO 추가", description = "매장 공유 TODO 직접 추가. AI 추출 결과 저장 시에도 이 API 사용")
    ApiResponse<TodoResponse> create(
            @Parameter(hidden = true) Long userId,
            TodoCreateRequest request);

    @Operation(summary = "매장 TODO 목록 조회", description = "해당 매장 전체 TODO 최신순 반환")
    ApiResponse<List<TodoResponse>> getMyTodos(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "가게 ID") Long storeId);

    @Operation(summary = "TODO 완료 토글", description = "완료 ↔ 미완료 토글. idempotent")
    ApiResponse<TodoResponse> toggleDone(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "TODO ID") Long todoId);

    @Operation(summary = "TODO 삭제", description = "같은 매장 소속이면 누구든 삭제 가능 (소유권 체크 없음)")
    ApiResponse<Void> delete(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "TODO ID") Long todoId);
}