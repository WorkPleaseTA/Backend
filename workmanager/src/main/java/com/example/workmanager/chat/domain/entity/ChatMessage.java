package com.example.workmanager.chat.domain.entity;

import com.example.workmanager.global.common.BaseEntity;
import com.example.workmanager.store.domain.entity.StoreMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_member_id", nullable = false)
    private StoreMember storeMember;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    @Builder
    private ChatMessage(ChatRoom chatRoom, StoreMember storeMember, String content, MessageType type) {
        this.chatRoom = chatRoom;
        this.storeMember = storeMember;
        this.content = content;
        this.type = type;
    }
}