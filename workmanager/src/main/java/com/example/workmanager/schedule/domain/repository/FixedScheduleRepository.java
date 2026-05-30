package com.example.workmanager.schedule.domain.repository;

import com.example.workmanager.schedule.domain.entity.FixedSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface FixedScheduleRepository extends JpaRepository<FixedSchedule, Long> {

    List<FixedSchedule> findAllByStoreMemberStoreId(Long storeId);

    List<FixedSchedule> findAllByStoreMemberStoreIdAndDayOfWeek(Long storeId, DayOfWeek dayOfWeek);

    boolean existsByStoreMemberIdAndDayOfWeek(Long storeMemberId, DayOfWeek dayOfWeek);

    Optional<FixedSchedule> findByStoreMemberIdAndDayOfWeek(Long storeMemberId, DayOfWeek dayOfWeek);
}