package com.example.workmanager.chat.domain.repository;

import com.example.workmanager.chat.domain.entity.ChatRoom;
import com.example.workmanager.chat.domain.entity.ChatRoomType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByStoreIdAndType(Long storeId, ChatRoomType type);
}