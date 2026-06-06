package com.example.workmanager.member.application.service;

import com.example.workmanager.global.auth.RefreshToken;
import com.example.workmanager.global.auth.RefreshTokenRepository;
import com.example.workmanager.global.common.exception.BaseException;
import com.example.workmanager.global.security.JwtProvider;
import com.example.workmanager.member.application.dto.request.LoginRequest;
import com.example.workmanager.member.application.dto.request.SignUpRequest;
import com.example.workmanager.member.application.dto.response.TokenResponse;
import com.example.workmanager.member.domain.entity.User;
import com.example.workmanager.member.domain.exception.UserErrorCode;
import com.example.workmanager.member.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest request) {
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new BaseException(UserErrorCode.LOGIN_ID_ALREADY_EXISTS);
        }

        User user = User.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(request.getRole())
                .build();

        userRepository.save(user);
    }

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BaseException(UserErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getRole().name());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        refreshTokenRepository.save(
                new RefreshToken(user.getId(), refreshToken, jwtProvider.getRefreshTokenExpiration() / 1000)
        );

        return new TokenResponse(accessToken, refreshToken, user.getName(), user.getRole().name());
    }

    public TokenResponse reissue(String refreshTokenValue) {
        if (!jwtProvider.validateToken(refreshTokenValue)) {
            throw new BaseException(UserErrorCode.INVALID_TOKEN);
        }

        Long userId = jwtProvider.getUserId(refreshTokenValue);

        RefreshToken saved = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new BaseException(UserErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (!saved.getToken().equals(refreshTokenValue)) {
            throw new BaseException(UserErrorCode.INVALID_TOKEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtProvider.createAccessToken(user.getId(), user.getRole().name());
        String newRefreshToken = jwtProvider.createRefreshToken(user.getId());

        refreshTokenRepository.save(
                new RefreshToken(userId, newRefreshToken, jwtProvider.getRefreshTokenExpiration())
        );

        return new TokenResponse(newAccessToken, newRefreshToken, user.getName(), user.getRole().name());
    }

    public void logout(Long userId) {
        refreshTokenRepository.deleteById(userId);
    }
}