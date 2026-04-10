package com.nnt.hotelbooking.auth.service.Impl;

import com.nnt.hotelbooking.auth.constants.AccountStatus;
import com.nnt.hotelbooking.auth.dto.request.RefreshTokenRequest;
import com.nnt.hotelbooking.auth.dto.response.RefreshTokenResponse;
import com.nnt.hotelbooking.auth.model.RefreshToken;
import com.nnt.hotelbooking.auth.repository.RefreshTokenRepository;
import com.nnt.hotelbooking.auth.service.AuthService;
import com.nnt.hotelbooking.auth.dto.request.LoginRequest;
import com.nnt.hotelbooking.auth.dto.response.LoginResponse;
import com.nnt.hotelbooking.auth.model.Auth;
import com.nnt.hotelbooking.auth.repository.AuthRepository;
import com.nnt.hotelbooking.auth.service.RedisSessionService;
import com.nnt.hotelbooking.auth.utils.JwtUtil;
import com.nnt.hotelbooking.common.exception.AppException;
import com.nnt.hotelbooking.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j(topic = "AUTH-SERVICE")
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisSessionService redisSessionService;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final int MAX_REFRESH_TOKENS = 2;

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("[LOGIN] Request | username={}",
                request.getUsername());

        Auth auth = authRepository.findByUsernameAndStatus(
                request.getUsername(),
                AccountStatus.ACTIVE
        ).orElseThrow(() -> new RuntimeException("Account not found"));

        if (!passwordEncoder.matches(request.getPassword(), auth.getPassword())) {
            throw new AppException(ErrorCode.INVALID_USERNAME_PASSWORD);
        }

        String deviceId = UUID.randomUUID().toString();

        String accessToken = jwtUtil.generateAccessToken(
                auth.getId(),
                auth.getUsername(),
                deviceId,
                auth.getAccessTokenVersion()
        );

        String refreshToken = jwtUtil.generateRefreshToken(
                auth.getId(),
                auth.getUsername(),
                deviceId
        );

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .tokenValue(refreshToken)
                        .authId(auth.getId())
                        .expiredAt(LocalDateTime.now().plusDays(7))
                        .build()
        );

        enforceMaxRefreshTokens(auth.getId());

        String jti = jwtUtil.extractJti(accessToken);
        long ttl = jwtUtil.getRemainingMillis(accessToken);
        redisSessionService.saveSession(auth.getId(), jti, accessToken, ttl);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .deviceId(deviceId)
                .build();
    }

    @Override
    public void logout(String accessToken) {
        Long userId = jwtUtil.extractUserId(accessToken);
        String jti = jwtUtil.extractJti(accessToken);
        long ttl = jwtUtil.getRemainingMillis(accessToken);

        redisSessionService.blacklistToken(jti, ttl);
        redisSessionService.removeActiveSession(userId, jti);
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        log.info("[REFRESH] Request");

        Claims claims = jwtUtil.extractClaims(refreshToken);

        RefreshToken savedToken = refreshTokenRepository
                .findByTokenValue(refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_INVALID));

        // Check token hết hạn trong DB
        if (savedToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            log.warn("[REFRESH] Token expired in DB | authId={}", savedToken.getAuthId());
            refreshTokenRepository.delete(savedToken);
            throw new AppException(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        Long userId = claims.get("userId", Long.class);
        String username = claims.getSubject();
        String deviceId = claims.get("deviceId", String.class);

        Auth auth = authRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtUtil.generateAccessToken(
                userId,
                username,
                deviceId,
                auth.getAccessTokenVersion()
        );

        String jti = jwtUtil.extractJti(newAccessToken);
        long ttl = jwtUtil.getRemainingMillis(newAccessToken);

        redisSessionService.saveSession(userId, jti, newAccessToken, ttl);

        log.info("[REFRESH] New access token generated | authId={} | deviceId={}",
                userId, deviceId);

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void enforceMaxRefreshTokens(Long authId) {
        List<RefreshToken> tokens =
                refreshTokenRepository.findByAuthIdOrderByCreatedAtAsc(authId);

        while (tokens.size() > MAX_REFRESH_TOKENS) {
            refreshTokenRepository.delete(tokens.get(0));
            tokens.remove(0);
        }
    }


    @Override
    public String checkToken(String accessToken) {
        try {
            String jti = jwtUtil.extractJti(accessToken);

            if (redisSessionService.isBlacklisted(jti)) {
                return "BLACKLISTED";
            }

            if (redisSessionService.isActive(jti)) {
                return "ACTIVE";
            }

            return "NOT_ACTIVE";

        } catch (Exception e) {
            return "INVALID";
        }
    }
}
