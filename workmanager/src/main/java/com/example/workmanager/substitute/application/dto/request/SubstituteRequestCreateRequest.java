package com.example.workmanager.substitute.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;

@Getter
public class SubstituteRequestCreateRequest {

    @NotNull
    private Long storeId;

    @NotNull
    private LocalDate requestDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotBlank
    private String message;

    @NotEmpty
    private List<Long> selectedStoreMemberIds;
}