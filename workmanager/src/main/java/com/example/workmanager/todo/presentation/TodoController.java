package com.example.workmanager.todo.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.todo.application.dto.request.TodoCreateRequest;
import com.example.workmanager.todo.application.dto.response.TodoResponse;
import com.example.workmanager.todo.application.service.TodoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController implements TodoControllerDocs {

    private final TodoService todoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TodoResponse> create(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid TodoCreateRequest request) {
        return ApiResponse.success(todoService.create(userId, request));
    }

    @GetMapping
    public ApiResponse<List<TodoResponse>> getMyTodos(
            @AuthenticationPrincipal Long userId,
            @RequestParam Long storeId) {
        return ApiResponse.success(todoService.getStoreTodos(userId, storeId));
    }

    @PatchMapping("/{todoId}/done")
    public ApiResponse<TodoResponse> toggleDone(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long todoId) {
        return ApiResponse.success(todoService.toggleDone(userId, todoId));
    }

    @DeleteMapping("/{todoId}")
    public ApiResponse<Void> delete(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long todoId) {
        todoService.delete(userId, todoId);
        return ApiResponse.success("TODO가 삭제되었습니다.");
    }
}