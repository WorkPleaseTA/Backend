package com.example.workmanager.substitute.domain.entity;

import com.example.workmanager.global.common.BaseEntity;
import com.example.workmanager.store.domain.entity.StoreMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "substitute_candidates")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubstituteCandidate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substitute_request_id", nullable = false)
    private SubstituteRequest substituteRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_member_id", nullable = false)
    private StoreMember storeMember;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubstituteCandidateStatus status;

    @Builder
    private SubstituteCandidate(SubstituteRequest substituteRequest, StoreMember storeMember) {
        this.substituteRequest = substituteRequest;
        this.storeMember = storeMember;
        this.status = SubstituteCandidateStatus.WAITING;
    }

    public void accept() {
        this.status = SubstituteCandidateStatus.ACCEPTED;
    }

    public void decline() {
        this.status = SubstituteCandidateStatus.DECLINED;
    }
}