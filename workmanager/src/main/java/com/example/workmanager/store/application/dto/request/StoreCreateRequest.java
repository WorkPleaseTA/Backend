package com.example.workmanager.store.application.dto.request;

import com.example.workmanager.store.domain.entity.BusinessType;
import com.example.workmanager.store.domain.entity.StoreSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreCreateRequest {

    @NotBlank(message = "가게 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "업종을 선택해주세요.")
    private BusinessType businessType;

    @NotNull(message = "규모를 선택해주세요.")
    private StoreSize size;
}