package com.example.workmanager.schedule.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
public class FixedScheduleUpdateRequest {

    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    @Valid
    private List<ScheduleEntry> schedules;

    @Getter
    public static class ScheduleEntry {

        @NotNull
        private Long storeMemberId;

        @NotNull
        private LocalTime startTime;

        @NotNull
        private LocalTime endTime;
    }
}