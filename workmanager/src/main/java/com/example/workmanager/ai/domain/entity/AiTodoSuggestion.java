package com.example.workmanager.ai.domain.entity;

import com.example.workmanager.chat.domain.entity.ChatRoom;
import com.example.workmanager.global.common.BaseEntity;
import com.example.workmanager.store.domain.entity.StoreMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ai_todo_suggestions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiTodoSuggestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_member_id", nullable = false)
    private StoreMember requestedBy;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String suggestionsJson;

    @Builder
    private AiTodoSuggestion(ChatRoom chatRoom, StoreMember requestedBy, String suggestionsJson) {
        this.chatRoom = chatRoom;
        this.requestedBy = requestedBy;
        this.suggestionsJson = suggestionsJson;
    }
}