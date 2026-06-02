package com.example.workmanager.substitute.application.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class SubstituteRequestCreateRequest {

    private Long storeId;
    private LocalDate requestDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
}