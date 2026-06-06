package com.example.workmanager.todo.application.dto.response;

import com.example.workmanager.todo.domain.entity.Todo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TodoResponse {

    private Long id;
    private String content;
    private boolean isDone;
    private LocalDateTime createdAt;

    public static TodoResponse from(Todo todo) {
        return TodoResponse.builder()
                .id(todo.getId())
                .content(todo.getContent())
                .isDone(todo.isDone())
                .createdAt(todo.getCreatedAt())
                .build();
    }
}