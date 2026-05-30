package com.example.workmanager.schedule.application.dto.response;

import com.example.workmanager.schedule.domain.entity.FixedSchedule;
import com.example.workmanager.store.domain.entity.StoreMember;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class FixedScheduleEditResponse {

    private Long storeMemberId;
    private String staffName;
    private Long fixedScheduleId;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean registered;

    public static FixedScheduleEditResponse of(StoreMember member, FixedSchedule schedule) {
        if (schedule == null) {
            return FixedScheduleEditResponse.builder()
                    .storeMemberId(member.getId())
                    .staffName(member.getUser().getName())
                    .fixedScheduleId(null)
                    .startTime(null)
                    .endTime(null)
                    .registered(false)
                    .build();
        }
        return FixedScheduleEditResponse.builder()
                .storeMemberId(member.getId())
                .staffName(member.getUser().getName())
                .fixedScheduleId(schedule.getId())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .registered(true)
                .build();
    }
}