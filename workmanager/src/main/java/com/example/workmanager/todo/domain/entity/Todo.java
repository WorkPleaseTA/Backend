package com.example.workmanager.todo.domain.entity;

import com.example.workmanager.global.common.BaseEntity;
import com.example.workmanager.store.domain.entity.StoreMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "todos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_member_id", nullable = false)
    private StoreMember storeMember;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isDone = false;

    @Builder
    private Todo(StoreMember storeMember, String content) {
        this.storeMember = storeMember;
        this.content = content;
        this.isDone = false;
    }

    public void toggleDone() {
        this.isDone = !this.isDone;
    }
}