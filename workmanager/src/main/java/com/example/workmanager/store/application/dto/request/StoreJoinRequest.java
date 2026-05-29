package com.example.workmanager.store.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreJoinRequest {

    @NotBlank(message = "초대 코드를 입력해주세요.")
    private String inviteCode;
}