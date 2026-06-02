package com.example.workmanager.substitute.application.dto.request;

import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class StaffAvailabilityCreateRequest {

    private Long storeId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}