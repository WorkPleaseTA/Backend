package com.example.workmanager.chat.application.dto.response;

import com.example.workmanager.chat.domain.entity.ChatMessage;
import com.example.workmanager.chat.domain.entity.MessageType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageResponse {

    private Long id;
    private Long storeMemberId;
    private String senderName;
    private String content;
    private MessageType type;
    private boolean isMine;
    private LocalDateTime createdAt;

    public static ChatMessageResponse of(ChatMessage message, Long myStoreMemberId) {
        return ChatMessageResponse.builder()
                .id(message.getId())
                .storeMemberId(message.getStoreMember().getId())
                .senderName(message.getStoreMember().getUser().getName())
                .content(message.getContent())
                .type(message.getType())
                .isMine(message.getStoreMember().getId().equals(myStoreMemberId))
                .createdAt(message.getCreatedAt())
                .build();
    }

    public static ChatMessageResponse broadcast(ChatMessage message) {
        return of(message, -1L);
    }
}