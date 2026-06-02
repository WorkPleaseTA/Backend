package com.example.workmanager.substitute.application.dto.response;

import com.example.workmanager.schedule.domain.entity.FixedSchedule;
import com.example.workmanager.substitute.domain.entity.StaffAvailability;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AvailabilityGridResponse {

    private List<FixedSlot> fixedSchedules;
    private List<AvailabilitySlot> availabilities;

    @Getter
    @AllArgsConstructor
    public static class FixedSlot {
        private DayOfWeek dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;

        public static FixedSlot from(FixedSchedule schedule) {
            return new FixedSlot(schedule.getDayOfWeek(), schedule.getStartTime(), schedule.getEndTime());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class AvailabilitySlot {
        private Long id;
        private DayOfWeek dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;

        public static AvailabilitySlot from(StaffAvailability availability) {
            return new AvailabilitySlot(
                    availability.getId(),
                    availability.getDayOfWeek(),
                    availability.getStartTime(),
                    availability.getEndTime()
            );
        }
    }
}