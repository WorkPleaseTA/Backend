package com.example.workmanager.substitute.domain.repository;

import com.example.workmanager.substitute.domain.entity.StaffAvailability;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffAvailabilityRepository extends JpaRepository<StaffAvailability, Long> {

    List<StaffAvailability> findAllByStoreMemberId(Long storeMemberId);

    List<StaffAvailability> findAllByStoreMemberStoreIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
            Long storeId, DayOfWeek dayOfWeek, LocalTime endTime, LocalTime startTime);

    boolean existsByStoreMemberIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
            Long storeMemberId, DayOfWeek dayOfWeek, LocalTime endTime, LocalTime startTime);
}