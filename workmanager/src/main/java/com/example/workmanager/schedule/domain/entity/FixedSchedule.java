package com.example.workmanager.schedule.domain.entity;

import com.example.workmanager.global.common.BaseEntity;
import com.example.workmanager.store.domain.entity.StoreMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "fixed_schedules",
        uniqueConstraints = @UniqueConstraint(columnNames = {"store_member_id", "day_of_week"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FixedSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_member_id", nullable = false)
    private StoreMember storeMember;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Builder
    private FixedSchedule(StoreMember storeMember, DayOfWeek dayOfWeek,
                          LocalTime startTime, LocalTime endTime) {
        this.storeMember = storeMember;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void update(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
