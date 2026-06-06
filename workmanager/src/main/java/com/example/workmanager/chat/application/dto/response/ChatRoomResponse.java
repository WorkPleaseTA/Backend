package com.example.workmanager.chat.application.dto.response;

import com.example.workmanager.chat.domain.entity.ChatRoomType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponse {

    private Long roomId;
    private ChatRoomType type;
    private String name;
    private int memberCount;
    private Long partnerStoreMemberId;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private long unreadCount;
}