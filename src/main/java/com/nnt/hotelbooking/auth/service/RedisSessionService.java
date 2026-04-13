package com.nnt.hotelbooking.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSessionService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String USER_SESSIONS_KEY = "hb:auth:user:";
    private static final String DEVICE_KEY = "hb:auth:device:";
    private static final String TOKEN_KEY = "hb:auth:token:";
    private static final String BLACKLIST_KEY = "hb:auth:blacklist:";
    private static final int MAX_DEVICES = 2;

    public void saveSession(
            Long userId,
            String jti,
            String token,
            long ttlMillis,
            String deviceId
    ) {
        String listKey = USER_SESSIONS_KEY + userId;
        String deviceKey = DEVICE_KEY + userId + ":" + deviceId;

        // refresh cùng device -> remove device cũ khỏi list
        Object existingJti = redisTemplate.opsForValue().get(deviceKey);

        if (existingJti != null) {
            redisTemplate.opsForList().remove(listKey, 1, deviceId);

            String oldJti = existingJti.toString();

            Long oldTtl = redisTemplate.getExpire(
                    TOKEN_KEY + oldJti,
                    TimeUnit.MILLISECONDS
            );

            blacklistToken(
                    oldJti,
                    oldTtl != null && oldTtl > 0 ? oldTtl : ttlMillis
            );

            redisTemplate.delete(TOKEN_KEY + oldJti);
        }

        // save token
        redisTemplate.opsForValue().set(
                TOKEN_KEY + jti,
                token,
                ttlMillis,
                TimeUnit.MILLISECONDS
        );

        // update device -> jti
        redisTemplate.opsForValue().set(
                deviceKey,
                jti,
                ttlMillis,
                TimeUnit.MILLISECONDS
        );

        // push newest device
        redisTemplate.opsForList().rightPush(listKey, deviceId);

        // enforce max devices
        Long size = redisTemplate.opsForList().size(listKey);

        if (size != null && size > MAX_DEVICES) {
            Object oldestDevice = redisTemplate.opsForList().leftPop(listKey);

            if (oldestDevice != null) {
                String oldDeviceId = oldestDevice.toString();
                String oldDeviceKey = DEVICE_KEY + userId + ":" + oldDeviceId;

                Object oldJtiObj = redisTemplate.opsForValue().get(oldDeviceKey);

                if (oldJtiObj != null) {
                    String oldJti = oldJtiObj.toString();

                    Long oldTtl = redisTemplate.getExpire(
                            TOKEN_KEY + oldJti,
                            TimeUnit.MILLISECONDS
                    );

                    blacklistToken(
                            oldJti,
                            oldTtl != null && oldTtl > 0 ? oldTtl : ttlMillis
                    );

                    redisTemplate.delete(TOKEN_KEY + oldJti);
                    redisTemplate.delete(oldDeviceKey);

                    log.warn("[SESSION] Kick oldest device | userId={} | deviceId={}",
                            userId, oldDeviceId);
                }
            }
        }

        redisTemplate.expire(listKey, ttlMillis, TimeUnit.MILLISECONDS);
    }

    public void blacklistToken(String jti, long ttlMillis) {
        redisTemplate.opsForValue().set(
                BLACKLIST_KEY + jti,
                "LOGGED_OUT",
                ttlMillis,
                TimeUnit.MILLISECONDS
        );
    }

    public boolean isBlacklisted(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_KEY + jti));
    }

    public boolean isActive(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(TOKEN_KEY + jti));
    }

    public String getCurrentJti(Long userId, String deviceId) {
        String deviceKey = DEVICE_KEY + userId + ":" + deviceId;
        Object value = redisTemplate.opsForValue().get(deviceKey);
        return value == null ? null : value.toString();
    }

    public void removeActiveSession(Long userId, String jti, String deviceId) {
        String listKey = USER_SESSIONS_KEY + userId;
        String deviceKey = DEVICE_KEY + userId + ":" + deviceId;

        redisTemplate.opsForList().remove(listKey, 1, deviceId);
        redisTemplate.delete(deviceKey);
        redisTemplate.delete(TOKEN_KEY + jti);
    }
}