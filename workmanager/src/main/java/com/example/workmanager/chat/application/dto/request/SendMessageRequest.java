package com.example.workmanager.chat.application.dto.request;

import com.example.workmanager.chat.domain.entity.MessageType;
import lombok.Getter;

@Getter
public class SendMessageRequest {

    private String content;
    private MessageType type = MessageType.TEXT;
}