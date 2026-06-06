package com.example.workmanager.store.application.dto.response;

import com.example.workmanager.member.domain.entity.UserRole;
import com.example.workmanager.store.domain.entity.Store;
import com.example.workmanager.store.domain.entity.StoreMember;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreDetailResponse {

    private Long id;
    private Long myStoreMemberId;
    private String name;
    private String businessType;
    private String size;
    private String inviteCode;
    private List<StaffInfo> staffList;

    public static StoreDetailResponse of(Store store, List<StoreMember> members,
                                          Map<Long, List<DayOfWeek>> workDaysMap,
                                          Long myStoreMemberId) {
        List<StaffInfo> staffList = members.stream()
                .filter(m -> m.getUser().getRole() != UserRole.OWNER)
                .map(m -> new StaffInfo(
                        m.getUser().getId(),
                        m.getUser().getName(),
                        workDaysMap.getOrDefault(m.getId(), List.of())
                ))
                .toList();

        return StoreDetailResponse.builder()
                .id(store.getId())
                .myStoreMemberId(myStoreMemberId)
                .name(store.getName())
                .businessType(store.getBusinessType().getLabel())
                .size(store.getSize().getLabel())
                .inviteCode(store.getInviteCode())
                .staffList(staffList)
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class StaffInfo {
        private Long userId;
        private String name;
        private List<DayOfWeek> workDays;
    }
}