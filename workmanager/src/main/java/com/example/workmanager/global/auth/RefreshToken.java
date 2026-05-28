package com.example.workmanager.global.auth;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("refresh_token")
public class RefreshToken {

    @Id
    private Long userId;
    private String token;

    @TimeToLive
    private long ttl; // seconds

    public RefreshToken(Long userId, String token, long ttl) {
        this.userId = userId;
        this.token = token;
        this.ttl = ttl;
    }
}