package com.example.workmanager.ai.application.service;

import com.example.workmanager.ai.application.dto.response.AiTodoExtractResponse;
import com.example.workmanager.ai.domain.entity.AiTodoSuggestion;
import com.example.workmanager.ai.domain.exception.AiErrorCode;
import com.example.workmanager.ai.domain.repository.AiTodoSuggestionRepository;
import com.example.workmanager.chat.domain.entity.ChatMessage;
import com.example.workmanager.chat.domain.entity.MessageType;
import com.example.workmanager.chat.domain.repository.ChatMessageRepository;
import com.example.workmanager.chat.domain.repository.ChatRoomMemberRepository;
import com.example.workmanager.chat.domain.repository.ChatRoomRepository;
import com.example.workmanager.global.common.exception.BaseException;
import com.example.workmanager.store.domain.entity.StoreMember;
import com.example.workmanager.store.domain.entity.StoreMemberStatus;
import com.example.workmanager.store.domain.repository.StoreMemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AiTodoService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final StoreMemberRepository storeMemberRepository;
    private final AiTodoSuggestionRepository aiTodoSuggestionRepository;
    private final AiService aiService;
    private final ObjectMapper objectMapper;

    public AiTodoExtractResponse extractTodos(Long userId, Long roomId) {
        var chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BaseException(AiErrorCode.CHAT_ROOM_NOT_FOUND));

        StoreMember storeMember = storeMemberRepository
                .findByUserIdAndStoreIdAndStatus(userId, chatRoom.getStore().getId(), StoreMemberStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(AiErrorCode.CHAT_ROOM_ACCESS_DENIED));

        chatRoomMemberRepository.findByChatRoomIdAndStoreMemberId(roomId, storeMember.getId())
                .orElseThrow(() -> new BaseException(AiErrorCode.CHAT_ROOM_ACCESS_DENIED));

        List<String> formattedMessages = chatMessageRepository
                .findByChatRoomIdOrderByIdDesc(roomId, PageRequest.of(0, 50))
                .stream()
                .filter(m -> m.getType() == MessageType.TEXT)
                .sorted(Comparator.comparingLong(ChatMessage::getId))
                .map(m -> m.getStoreMember().getUser().getName() + ": " + m.getContent())
                .toList();

        List<String> todos = aiService.extractTodos(formattedMessages);

        saveSuggestion(chatRoom, storeMember, todos);

        return AiTodoExtractResponse.from(todos);
    }

    private void saveSuggestion(com.example.workmanager.chat.domain.entity.ChatRoom chatRoom,
                                 StoreMember storeMember, List<String> todos) {
        try {
            aiTodoSuggestionRepository.save(AiTodoSuggestion.builder()
                    .chatRoom(chatRoom)
                    .requestedBy(storeMember)
                    .suggestionsJson(objectMapper.writeValueAsString(todos))
                    .build());
        } catch (Exception ignored) {}
    }
}