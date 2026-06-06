package com.example.workmanager.schedule.application.dto.response;

import com.example.workmanager.schedule.domain.entity.FixedSchedule;
import com.example.workmanager.schedule.domain.entity.WorkSchedule;
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
    private boolean isSubstitute;

    public static TodayScheduleResponse from(FixedSchedule schedule) {
        return TodayScheduleResponse.builder()
                .storeId(schedule.getStoreMember().getStore().getId())
                .storeName(schedule.getStoreMember().getStore().getName())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .isSubstitute(false)
                .build();
    }

    public static TodayScheduleResponse fromWorkSchedule(WorkSchedule workSchedule) {
        return TodayScheduleResponse.builder()
                .storeId(workSchedule.getStoreMember().getStore().getId())
                .storeName(workSchedule.getStoreMember().getStore().getName())
                .startTime(workSchedule.getStartTime())
                .endTime(workSchedule.getEndTime())
                .isSubstitute(true)
                .build();
    }
}
