package com.example.workmanager.ai.application.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiTodoExtractResponse {

    private List<String> todos;

    public static AiTodoExtractResponse from(List<String> todos) {
        return AiTodoExtractResponse.builder()
                .todos(todos)
                .build();
    }
}