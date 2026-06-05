package com.example.workmanager.ai.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AiTodoExtractRequest {

    @NotNull
    private Long roomId;
}