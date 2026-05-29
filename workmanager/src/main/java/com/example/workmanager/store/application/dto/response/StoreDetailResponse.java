package com.example.workmanager.store.application.dto.response;

import com.example.workmanager.store.domain.entity.Store;
import com.example.workmanager.store.domain.entity.StoreMember;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreDetailResponse {

    private Long id;
    private String name;
    private String businessType;
    private String size;
    private String inviteCode;
    private List<StaffInfo> staffList;

    public static StoreDetailResponse of(Store store, List<StoreMember> members) {
        List<StaffInfo> staffList = members.stream()
                .map(m -> new StaffInfo(m.getUser().getId(), m.getUser().getName()))
                .toList();

        return StoreDetailResponse.builder()
                .id(store.getId())
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
        // workDays: schedule 기능 구현 시 추가 예정 (CLAUDE.md 참고)
    }
}