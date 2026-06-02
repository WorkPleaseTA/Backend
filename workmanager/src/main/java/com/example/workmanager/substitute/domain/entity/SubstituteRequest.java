package com.example.workmanager.substitute.domain.entity;

import com.example.workmanager.global.common.BaseEntity;
import com.example.workmanager.store.domain.entity.StoreMember;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "substitute_requests")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubstituteRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_member_id", nullable = false)
    private StoreMember storeMember;

    @Column(nullable = false)
    private LocalDate requestDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubstituteRequestStatus status;

    @Builder
    private SubstituteRequest(StoreMember storeMember, LocalDate requestDate,
                               LocalTime startTime, LocalTime endTime, String reason) {
        this.storeMember = storeMember;
        this.requestDate = requestDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.status = SubstituteRequestStatus.PENDING;
    }

    public void accept() {
        this.status = SubstituteRequestStatus.ACCEPTED;
    }

    public void cancel() {
        this.status = SubstituteRequestStatus.CANCELLED;
    }
}
