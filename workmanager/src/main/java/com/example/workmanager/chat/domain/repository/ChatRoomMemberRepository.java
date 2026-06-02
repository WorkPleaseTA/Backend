package com.example.workmanager.chat.domain.repository;

import com.example.workmanager.chat.domain.entity.ChatRoomMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    List<ChatRoomMember> findAllByStoreMemberId(Long storeMemberId);

    List<ChatRoomMember> findAllByChatRoomId(Long chatRoomId);

    Optional<ChatRoomMember> findByChatRoomIdAndStoreMemberId(Long chatRoomId, Long storeMemberId);

    long countByChatRoomId(Long chatRoomId);
}