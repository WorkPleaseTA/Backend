package com.example.workmanager.member.presentation;

import com.example.workmanager.global.common.response.ApiResponse;
import com.example.workmanager.member.application.dto.request.LoginRequest;
import com.example.workmanager.member.application.dto.request.ReissueRequest;
import com.example.workmanager.member.application.dto.request.SignUpRequest;
import com.example.workmanager.member.application.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "회원가입 / 로그인 / 토큰 관리")
public interface AuthControllerDocs {

    @Operation(summary = "회원가입", description = "role: OWNER(사장님) 또는 STAFF(직원)")
    ApiResponse<Void> signUp(SignUpRequest request);

    @Operation(summary = "로그인", description = "로그인 성공 시 accessToken + refreshToken 반환")
    ApiResponse<TokenResponse> login(LoginRequest request);

    @Operation(summary = "토큰 재발급", description = "refreshToken으로 새 accessToken/refreshToken 발급")
    ApiResponse<TokenResponse> reissue(ReissueRequest request);

    @Operation(summary = "로그아웃", description = "Redis에 저장된 refreshToken 삭제")
    ApiResponse<Void> logout(@Parameter(hidden = true) Long userId);
}