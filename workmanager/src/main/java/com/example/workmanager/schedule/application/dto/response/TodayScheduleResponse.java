package com.example.workmanager.schedule.application.dto.response;

import com.example.workmanager.schedule.domain.entity.FixedSchedule;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodayScheduleResponse {

    private Long storeId;
    private String storeName;
    private LocalTime startTime;
    private LocalTime endTime;

    public static TodayScheduleResponse from(FixedSchedule schedule) {
        return TodayScheduleResponse.builder()
                .storeId(schedule.getStoreMember().getStore().getId())
                .storeName(schedule.getStoreMember().getStore().getName())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .build();
    }
}
