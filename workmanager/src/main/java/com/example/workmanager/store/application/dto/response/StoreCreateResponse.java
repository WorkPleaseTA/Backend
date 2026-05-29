package com.example.workmanager.store.application.dto.response;

import com.example.workmanager.store.domain.entity.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreCreateResponse {

    private Long id;
    private String name;
    private String businessType;
    private String size;
    private String inviteCode;

    public static StoreCreateResponse from(Store store) {
        return StoreCreateResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .businessType(store.getBusinessType().getLabel())
                .size(store.getSize().getLabel())
                .inviteCode(store.getInviteCode())
                .build();
    }
}