package com.example.workmanager.store.domain.repository;

import com.example.workmanager.store.domain.entity.StoreMember;
import com.example.workmanager.store.domain.entity.StoreMemberStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreMemberRepository extends JpaRepository<StoreMember, Long> {

    boolean existsByStoreIdAndUserId(Long storeId, Long userId);

    List<StoreMember> findAllByUserIdAndStatus(Long userId, StoreMemberStatus status);

    List<StoreMember> findAllByStoreIdAndStatus(Long storeId, StoreMemberStatus status);

    Optional<StoreMember> findByIdAndStoreId(Long id, Long storeId);
}