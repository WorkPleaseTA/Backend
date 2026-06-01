package com.example.workmanager.store.application.service;

import com.example.workmanager.global.common.exception.BaseException;
import com.example.workmanager.member.domain.entity.User;
import com.example.workmanager.member.domain.entity.UserRole;
import com.example.workmanager.member.domain.exception.UserErrorCode;
import com.example.workmanager.member.domain.repository.UserRepository;
import com.example.workmanager.store.application.dto.request.StoreCreateRequest;
import com.example.workmanager.store.application.dto.request.StoreJoinRequest;
import com.example.workmanager.store.application.dto.response.StoreCreateResponse;
import com.example.workmanager.store.application.dto.response.StoreDetailResponse;
import com.example.workmanager.store.domain.entity.Store;
import com.example.workmanager.store.domain.entity.StoreMember;
import com.example.workmanager.store.domain.entity.StoreMemberStatus;
import com.example.workmanager.store.domain.exception.StoreErrorCode;
import com.example.workmanager.store.domain.repository.StoreMemberRepository;
import com.example.workmanager.store.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMemberRepository storeMemberRepository;
    private final UserRepository userRepository;

    public StoreCreateResponse createStore(Long userId, StoreCreateRequest request) {
        User owner = getUser(userId);

        if (owner.getRole() != UserRole.OWNER) {
            throw new BaseException(StoreErrorCode.ACCESS_DENIED);
        }

        String inviteCode = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();

        Store store = Store.builder()
                .name(request.getName())
                .businessType(request.getBusinessType())
                .size(request.getSize())
                .inviteCode(inviteCode)
                .owner(owner)
                .build();

        return StoreCreateResponse.from(storeRepository.save(store));
    }

    public void joinStore(Long userId, StoreJoinRequest request) {
        User staff = getUser(userId);

        if (staff.getRole() != UserRole.STAFF) {
            throw new BaseException(StoreErrorCode.ACCESS_DENIED);
        }

        Store store = storeRepository.findByInviteCode(request.getInviteCode())
                .orElseThrow(() -> new BaseException(StoreErrorCode.INVALID_INVITE_CODE));

        if (storeMemberRepository.existsByStoreIdAndUserId(store.getId(), userId)) {
            throw new BaseException(StoreErrorCode.ALREADY_JOINED);
        }

        storeMemberRepository.save(StoreMember.builder()
                .store(store)
                .user(staff)
                .status(StoreMemberStatus.ACTIVE)
                .build());
    }

    @Transactional(readOnly = true)
    public List<StoreDetailResponse> getMyStores(Long userId) {
        User owner = getUser(userId);

        if (owner.getRole() != UserRole.OWNER) {
            throw new BaseException(StoreErrorCode.ACCESS_DENIED);
        }

        return storeRepository.findAllByOwnerId(userId).stream()
                .map(store -> {
                    List<StoreMember> members = storeMemberRepository
                            .findAllByStoreIdAndStatus(store.getId(), StoreMemberStatus.ACTIVE);
                    return StoreDetailResponse.of(store, members);
                })
                .toList();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));
    }
}