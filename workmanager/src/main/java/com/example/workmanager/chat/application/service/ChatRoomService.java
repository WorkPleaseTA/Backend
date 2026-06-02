package com.example.workmanager.chat.application.service;

import com.example.workmanager.chat.application.dto.request.ReadMessageRequest;
import com.example.workmanager.chat.application.dto.request.SendMessageRequest;
import com.example.workmanager.chat.application.dto.response.ChatMessageResponse;
import com.example.workmanager.chat.application.dto.response.ChatRoomResponse;
import com.example.workmanager.chat.domain.entity.*;
import com.example.workmanager.chat.domain.exception.ChatErrorCode;
import com.example.workmanager.chat.domain.repository.ChatMessageRepository;
import com.example.workmanager.chat.domain.repository.ChatRoomMemberRepository;
import com.example.workmanager.chat.domain.repository.ChatRoomRepository;
import com.example.workmanager.global.common.exception.BaseException;
import com.example.workmanager.store.domain.entity.Store;
import com.example.workmanager.store.domain.entity.StoreMember;
import com.example.workmanager.store.domain.entity.StoreMemberStatus;
import com.example.workmanager.store.domain.exception.StoreErrorCode;
import com.example.workmanager.store.domain.repository.StoreMemberRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private static final int MESSAGE_PAGE_SIZE = 30;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final StoreMemberRepository storeMemberRepository;

    // ── 채팅방 초기화 (StoreService에서 호출) ──────────────────────────────────

    public void initializeGroupRoom(Store store, StoreMember ownerMember) {
        ChatRoom groupRoom = chatRoomRepository.save(ChatRoom.builder()
                .store(store)
                .type(ChatRoomType.GROUP)
                .build());

        chatRoomMemberRepository.save(ChatRoomMember.builder()
                .chatRoom(groupRoom)
                .storeMember(ownerMember)
                .build());
    }

    public void onMemberJoined(StoreMember newMember, List<StoreMember> existingMembers, Store store) {
        ChatRoom groupRoom = chatRoomRepository.findByStoreIdAndType(store.getId(), ChatRoomType.GROUP)
                .orElseThrow(() -> new BaseException(ChatErrorCode.GROUP_ROOM_NOT_FOUND));

        chatRoomMemberRepository.save(ChatRoomMember.builder()
                .chatRoom(groupRoom)
                .storeMember(newMember)
                .build());

        for (StoreMember existing : existingMembers) {
            ChatRoom directRoom = chatRoomRepository.save(ChatRoom.builder()
                    .store(store)
                    .type(ChatRoomType.DIRECT)
                    .build());

            chatRoomMemberRepository.save(ChatRoomMember.builder()
                    .chatRoom(directRoom)
                    .storeMember(newMember)
                    .build());
            chatRoomMemberRepository.save(ChatRoomMember.builder()
                    .chatRoom(directRoom)
                    .storeMember(existing)
                    .build());
        }
    }

    // ── REST API ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getRooms(Long userId, Long storeId) {
        StoreMember me = getActiveMember(userId, storeId);

        List<ChatRoomMember> myRoomMemberships = chatRoomMemberRepository.findAllByStoreMemberId(me.getId());

        List<ChatRoomResponse> responses = new ArrayList<>();
        for (ChatRoomMember membership : myRoomMemberships) {
            ChatRoom room = membership.getChatRoom();

            ChatMessage lastMsg = chatMessageRepository
                    .findTopByChatRoomIdOrderByIdDesc(room.getId())
                    .orElse(null);

            long unreadCount = membership.getLastReadMessageId() == null
                    ? chatMessageRepository.countByChatRoomId(room.getId())
                    : chatMessageRepository.countByChatRoomIdAndIdGreaterThan(
                            room.getId(), membership.getLastReadMessageId());

            ChatRoomResponse.ChatRoomResponseBuilder builder = ChatRoomResponse.builder()
                    .roomId(room.getId())
                    .type(room.getType())
                    .lastMessage(lastMsg != null ? lastMsg.getContent() : null)
                    .lastMessageTime(lastMsg != null ? lastMsg.getCreatedAt() : null)
                    .unreadCount(unreadCount);

            if (room.getType() == ChatRoomType.GROUP) {
                builder.name("단체 채팅방")
                        .memberCount((int) chatRoomMemberRepository.countByChatRoomId(room.getId()));
            } else {
                ChatRoomMember partnerMembership = chatRoomMemberRepository
                        .findAllByChatRoomId(room.getId())
                        .stream()
                        .filter(m -> !m.getStoreMember().getId().equals(me.getId()))
                        .findFirst()
                        .orElseThrow();
                builder.name(partnerMembership.getStoreMember().getUser().getName())
                        .partnerStoreMemberId(partnerMembership.getStoreMember().getId())
                        .memberCount(2);
            }

            responses.add(builder.build());
        }

        responses.sort(Comparator
                .<ChatRoomResponse, Integer>comparing(r -> r.getType() == ChatRoomType.GROUP ? 0 : 1)
                .thenComparing(r -> r.getLastMessageTime() == null
                        ? java.time.LocalDateTime.MIN
                        : r.getLastMessageTime(), Comparator.reverseOrder()));

        return responses;
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getMessages(Long userId, Long roomId, Long cursor, int size) {
        StoreMember me = getActiveMemberByRoom(userId, roomId);

        int pageSize = Math.min(size, MESSAGE_PAGE_SIZE);
        List<ChatMessage> messages = cursor == null
                ? chatMessageRepository.findByChatRoomIdOrderByIdDesc(roomId, PageRequest.of(0, pageSize))
                : chatMessageRepository.findByChatRoomIdAndIdLessThanOrderByIdDesc(roomId, cursor, PageRequest.of(0, pageSize));

        List<ChatMessage> ascending = new ArrayList<>(messages);
        Collections.reverse(ascending);

        return ascending.stream()
                .map(msg -> ChatMessageResponse.of(msg, me.getId()))
                .toList();
    }

    public void readMessages(Long userId, Long roomId, ReadMessageRequest request) {
        StoreMember me = getActiveMemberByRoom(userId, roomId);

        ChatRoomMember membership = chatRoomMemberRepository
                .findByChatRoomIdAndStoreMemberId(roomId, me.getId())
                .orElseThrow(() -> new BaseException(ChatErrorCode.NOT_ROOM_MEMBER));

        membership.updateLastRead(request.getLastMessageId());
    }

    // ── WebSocket ─────────────────────────────────────────────────────────────

    public ChatMessageResponse saveMessage(Long roomId, Long userId, SendMessageRequest request) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BaseException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));

        StoreMember sender = getActiveMember(userId, room.getStore().getId());

        chatRoomMemberRepository.findByChatRoomIdAndStoreMemberId(roomId, sender.getId())
                .orElseThrow(() -> new BaseException(ChatErrorCode.NOT_ROOM_MEMBER));

        MessageType messageType = request.getType() != null ? request.getType() : MessageType.TEXT;

        ChatMessage message = chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(room)
                .storeMember(sender)
                .content(request.getContent())
                .type(messageType)
                .build());

        return ChatMessageResponse.broadcast(message);
    }

    // ── 공통 ──────────────────────────────────────────────────────────────────

    private StoreMember getActiveMember(Long userId, Long storeId) {
        return storeMemberRepository.findByUserIdAndStoreIdAndStatus(userId, storeId, StoreMemberStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(StoreErrorCode.ACCESS_DENIED));
    }

    private StoreMember getActiveMemberByRoom(Long userId, Long roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BaseException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));
        return getActiveMember(userId, room.getStore().getId());
    }
}