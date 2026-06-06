package com.example.workmanager.store.application.service;

import com.example.workmanager.chat.application.service.ChatRoomService;
import com.example.workmanager.global.common.exception.BaseException;
import com.example.workmanager.member.domain.entity.User;
import com.example.workmanager.member.domain.entity.UserRole;
import com.example.workmanager.member.domain.exception.UserErrorCode;
import com.example.workmanager.member.domain.repository.UserRepository;
import com.example.workmanager.schedule.domain.entity.FixedSchedule;
import com.example.workmanager.schedule.domain.repository.FixedScheduleRepository;
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

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMemberRepository storeMemberRepository;
    private final UserRepository userRepository;
    private final FixedScheduleRepository fixedScheduleRepository;
    private final ChatRoomService chatRoomService;

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

        Store savedStore = storeRepository.save(store);

        StoreMember ownerMember = storeMemberRepository.save(StoreMember.builder()
                .store(savedStore)
                .user(owner)
                .status(StoreMemberStatus.ACTIVE)
                .build());

        chatRoomService.initializeGroupRoom(savedStore, ownerMember);

        return StoreCreateResponse.from(savedStore);
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

        List<StoreMember> existingMembers =
                storeMemberRepository.findAllByStoreIdAndStatus(store.getId(), StoreMemberStatus.ACTIVE);

        StoreMember newMember = storeMemberRepository.save(StoreMember.builder()
                .store(store)
                .user(staff)
                .status(StoreMemberStatus.ACTIVE)
                .build());

        chatRoomService.onMemberJoined(newMember, existingMembers, store);
    }

    @Transactional(readOnly = true)
    public List<StoreDetailResponse> getMyStores(Long userId) {
        User user = getUser(userId);

        List<Store> stores = (user.getRole() == UserRole.OWNER)
                ? storeRepository.findAllByOwnerId(userId)
                : storeMemberRepository.findAllByUserIdAndStatus(userId, StoreMemberStatus.ACTIVE)
                        .stream()
                        .map(StoreMember::getStore)
                        .toList();

        return stores.stream()
                .map(store -> {
                    List<StoreMember> members = storeMemberRepository
                            .findAllByStoreIdAndStatus(store.getId(), StoreMemberStatus.ACTIVE);
                    Map<Long, List<DayOfWeek>> workDaysMap = fixedScheduleRepository
                            .findAllByStoreMemberStoreId(store.getId())
                            .stream()
                            .collect(Collectors.groupingBy(
                                    fs -> fs.getStoreMember().getId(),
                                    Collectors.mapping(FixedSchedule::getDayOfWeek, Collectors.toList())
                            ));
                    Long myStoreMemberId = storeMemberRepository
                            .findByUserIdAndStoreIdAndStatus(userId, store.getId(), StoreMemberStatus.ACTIVE)
                            .map(StoreMember::getId)
                            .orElse(null);
                    return StoreDetailResponse.of(store, members, workDaysMap, myStoreMemberId);
                })
                .toList();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));
    }
}