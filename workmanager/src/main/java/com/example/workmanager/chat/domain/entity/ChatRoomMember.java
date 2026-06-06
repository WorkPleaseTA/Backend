package com.example.workmanager.chat.domain.entity;

import com.example.workmanager.global.common.BaseEntity;
import com.example.workmanager.store.domain.entity.StoreMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_room_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"chat_room_id", "store_member_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_member_id", nullable = false)
    private StoreMember storeMember;

    @Column(name = "last_read_message_id")
    private Long lastReadMessageId;

    @Builder
    private ChatRoomMember(ChatRoom chatRoom, StoreMember storeMember) {
        this.chatRoom = chatRoom;
        this.storeMember = storeMember;
    }

    public void updateLastRead(Long messageId) {
        this.lastReadMessageId = messageId;
    }
}