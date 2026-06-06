package com.example.workmanager.substitute.application.dto.response;

import com.example.workmanager.substitute.domain.entity.StaffAvailability;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StaffAvailabilityResponse {

    private Long id;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public static StaffAvailabilityResponse from(StaffAvailability availability) {
        return StaffAvailabilityResponse.builder()
                .id(availability.getId())
                .dayOfWeek(availability.getDayOfWeek())
                .startTime(availability.getStartTime())
                .endTime(availability.getEndTime())
                .build();
    }
}