package com.nnt.hotelbooking.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisSessionService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ACTIVE_KEY = "hb:auth:active:";
    private static final String TOKEN_KEY = "hb:auth:token:";
    private static final String BLACKLIST_KEY = "hb:auth:blacklist:";

    public void saveSession(Long userId, String jti, String token, long ttlMillis) {
        String activeKey = ACTIVE_KEY + userId;
        String tokenKey = TOKEN_KEY + jti;

        long now = System.currentTimeMillis();

        redisTemplate.opsForZSet().add(activeKey, jti, now);
        redisTemplate.opsForValue().set(tokenKey, token, ttlMillis, TimeUnit.MILLISECONDS);

        enforceMaxTwoDevices(userId);
    }

    private void enforceMaxTwoDevices(Long userId) {
        String activeKey = ACTIVE_KEY + userId;
        Long size = redisTemplate.opsForZSet().zCard(activeKey);

        if (size != null && size > 2) {
            Set<Object> oldest = redisTemplate.opsForZSet().range(activeKey, 0, 0);
            if (oldest != null && !oldest.isEmpty()) {
                String oldestJti = oldest.iterator().next().toString();
                blacklistByJti(oldestJti, 3600000L);
                redisTemplate.opsForZSet().remove(activeKey, oldestJti);
                redisTemplate.delete(TOKEN_KEY + oldestJti);
            }
        }
    }

    public void blacklistToken(String jti, long ttlMillis) {
        redisTemplate.opsForValue().set(
                BLACKLIST_KEY + jti,
                "LOGGED_OUT",
                ttlMillis,
                TimeUnit.MILLISECONDS
        );
    }

    public void blacklistByJti(String jti, long ttlMillis) {
        blacklistToken(jti, ttlMillis);
    }

    public boolean isBlacklisted(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_KEY + jti));
    }

    public boolean isActive(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(TOKEN_KEY + jti));
    }

    public void removeActiveSession(Long userId, String jti) {
        redisTemplate.opsForZSet().remove(ACTIVE_KEY + userId, jti);
        redisTemplate.delete(TOKEN_KEY + jti);
    }
}