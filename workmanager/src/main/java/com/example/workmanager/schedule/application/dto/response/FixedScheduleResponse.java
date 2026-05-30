package com.example.workmanager.schedule.application.dto.response;

import com.example.workmanager.schedule.domain.entity.FixedSchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Builder
public class FixedScheduleResponse {

    private Long id;
    private Long storeMemberId;
    private String staffName;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public static FixedScheduleResponse from(FixedSchedule schedule) {
        return FixedScheduleResponse.builder()
                .id(schedule.getId())
                .storeMemberId(schedule.getStoreMember().getId())
                .staffName(schedule.getStoreMember().getUser().getName())
                .dayOfWeek(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .build();
    }
}