package com.example.workmanager.chat.presentation;

import com.example.workmanager.chat.application.dto.request.ReadMessageRequest;
import com.example.workmanager.chat.application.dto.response.ChatMessageResponse;
import com.example.workmanager.chat.application.dto.response.ChatRoomResponse;
import com.example.workmanager.chat.application.service.ChatRoomService;
import com.example.workmanager.global.common.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/rooms")
    public ApiResponse<List<ChatRoomResponse>> getRooms(
            @AuthenticationPrincipal Long userId,
            @RequestParam Long storeId) {
        return ApiResponse.success(chatRoomService.getRooms(userId, storeId));
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ApiResponse<List<ChatMessageResponse>> getMessages(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long roomId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "30") int size) {
        return ApiResponse.success(chatRoomService.getMessages(userId, roomId, cursor, size));
    }

    @PostMapping("/rooms/{roomId}/read")
    public ApiResponse<Void> readMessages(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long roomId,
            @RequestBody ReadMessageRequest request) {
        chatRoomService.readMessages(userId, roomId, request);
        return ApiResponse.success(null);
    }
}