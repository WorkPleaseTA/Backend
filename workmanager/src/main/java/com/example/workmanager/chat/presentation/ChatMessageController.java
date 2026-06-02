package com.example.workmanager.chat.presentation;

import com.example.workmanager.chat.application.dto.request.SendMessageRequest;
import com.example.workmanager.chat.application.dto.response.ChatMessageResponse;
import com.example.workmanager.chat.application.service.ChatRoomService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId,
                            @Payload SendMessageRequest request,
                            Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        ChatMessageResponse response = chatRoomService.saveMessage(roomId, userId, request);
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, response);
    }
}