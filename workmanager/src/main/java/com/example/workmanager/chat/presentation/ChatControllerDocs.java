package com.example.workmanager.chat.presentation;

import com.example.workmanager.chat.application.dto.request.ReadMessageRequest;
import com.example.workmanager.chat.application.dto.response.ChatMessageResponse;
import com.example.workmanager.chat.application.dto.response.ChatRoomResponse;
import com.example.workmanager.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Chat", description = "채팅방 목록 / 메시지 조회 / 읽음 처리 (메시지 전송은 WebSocket /app/chat/{roomId})")
public interface ChatControllerDocs {

    @Operation(summary = "채팅방 목록 조회",
            description = "단체(GROUP) + DM(DIRECT) 채팅방 목록. 미읽음 수, 마지막 메시지 포함")
    ApiResponse<List<ChatRoomResponse>> getRooms(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "가게 ID") Long storeId);

    @Operation(summary = "채팅 메시지 조회",
            description = "커서 기반 페이징 (최신순). cursor 미입력 시 최신 30개부터. 이전 메시지는 가장 오래된 messageId를 cursor로 전달")
    ApiResponse<List<ChatMessageResponse>> getMessages(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "채팅방 ID") Long roomId,
            @Parameter(description = "커서 (마지막으로 받은 messageId). 미입력 시 최신부터") Long cursor,
            @Parameter(description = "페이지 크기 (기본 30)") int size);

    @Operation(summary = "메시지 읽음 처리",
            description = "lastMessageId까지 읽음 처리. 해당 채팅방 미읽음 카운트 초기화")
    ApiResponse<Void> readMessages(
            @Parameter(hidden = true) Long userId,
            @Parameter(description = "채팅방 ID") Long roomId,
            ReadMessageRequest request);
}