package com.example.workmanager.schedule.domain.repository;

import com.example.workmanager.schedule.domain.entity.WorkSchedule;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {

    Optional<WorkSchedule> findByStoreMemberIdAndWorkDate(Long storeMemberId, LocalDate workDate);

    List<WorkSchedule> findAllByStoreMemberStoreIdAndWorkDate(Long storeId, LocalDate workDate);
}