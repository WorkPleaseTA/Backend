package com.example.workmanager.todo.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TodoCreateRequest {

    @NotNull
    private Long storeId;

    @NotBlank
    private String content;
}