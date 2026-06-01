package com.example.workmanager.store.domain.entity;

import com.example.workmanager.global.common.BaseEntity;
import com.example.workmanager.member.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stores")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusinessType businessType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreSize size;

    @Column(nullable = false, unique = true, length = 8)
    private String inviteCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Builder
    private Store(String name, BusinessType businessType, StoreSize size, String inviteCode, User owner) {
        this.name = name;
        this.businessType = businessType;
        this.size = size;
        this.inviteCode = inviteCode;
        this.owner = owner;
    }
}