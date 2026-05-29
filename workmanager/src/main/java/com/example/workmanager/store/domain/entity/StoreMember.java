package com.example.workmanager.store.domain.entity;

import com.example.workmanager.global.common.BaseEntity;
import com.example.workmanager.member.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"store_id", "user_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreMemberStatus status;

    @Builder
    private StoreMember(Store store, User user, StoreMemberStatus status) {
        this.store = store;
        this.user = user;
        this.status = status;
    }
}