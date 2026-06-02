package com.example.workmanager.chat.domain.repository;

import com.example.workmanager.chat.domain.entity.ChatMessage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Optional<ChatMessage> findTopByChatRoomIdOrderByIdDesc(Long chatRoomId);

    List<ChatMessage> findByChatRoomIdOrderByIdDesc(Long chatRoomId, Pageable pageable);

    List<ChatMessage> findByChatRoomIdAndIdLessThanOrderByIdDesc(Long chatRoomId, Long cursor, Pageable pageable);

    long countByChatRoomIdAndIdGreaterThan(Long chatRoomId, Long lastReadMessageId);

    long countByChatRoomId(Long chatRoomId);
}