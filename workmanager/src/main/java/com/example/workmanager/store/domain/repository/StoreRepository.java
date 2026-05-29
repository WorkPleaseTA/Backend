package com.example.workmanager.store.domain.repository;

import com.example.workmanager.store.domain.entity.Store;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAllByOwnerId(Long ownerId);

    Optional<Store> findByInviteCode(String inviteCode);
}
