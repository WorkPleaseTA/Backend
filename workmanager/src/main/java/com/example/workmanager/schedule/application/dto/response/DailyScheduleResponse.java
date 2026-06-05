package com.example.workmanager.schedule.application.dto.response;

import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailyScheduleResponse {

    private Long storeMemberId;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isSubstitute;
}