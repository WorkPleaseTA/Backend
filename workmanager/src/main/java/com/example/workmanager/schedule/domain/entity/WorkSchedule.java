package com.example.workmanager.schedule.domain.entity;

import com.example.workmanager.global.common.BaseEntity;
import com.example.workmanager.store.domain.entity.StoreMember;
import com.example.workmanager.substitute.domain.entity.SubstituteRequest;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "work_schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_member_id", nullable = false)
    private StoreMember storeMember;

    @Column(nullable = false)
    private LocalDate workDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substitute_request_id")
    private SubstituteRequest substituteRequest;

    @Builder
    private WorkSchedule(StoreMember storeMember, LocalDate workDate,
                         LocalTime startTime, LocalTime endTime,
                         SubstituteRequest substituteRequest) {
        this.storeMember = storeMember;
        this.workDate = workDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.substituteRequest = substituteRequest;
    }
}