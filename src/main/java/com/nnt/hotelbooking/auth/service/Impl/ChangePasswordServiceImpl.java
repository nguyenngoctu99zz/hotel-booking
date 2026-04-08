package com.nnt.hotelbooking.auth.service.Impl;
import com.nnt.hotelbooking.auth.dto.request.ChangePasswordRequest;
import com.nnt.hotelbooking.auth.model.Auth;
import com.nnt.hotelbooking.auth.repository.AuthRepository;
import com.nnt.hotelbooking.auth.repository.RefreshTokenRepository;
import com.nnt.hotelbooking.auth.service.ChangePasswordService;
import com.nnt.hotelbooking.auth.service.RedisSessionService;
import com.nnt.hotelbooking.auth.utils.JwtUtil;
import com.nnt.hotelbooking.common.exception.AppException;
import com.nnt.hotelbooking.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangePasswordServiceImpl implements ChangePasswordService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisSessionService redisSessionService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void changePassword(String accessToken, ChangePasswordRequest request) {
        Long userId = jwtUtil.extractUserId(accessToken);
        String jti = jwtUtil.extractJti(accessToken);
        long ttl = jwtUtil.getRemainingMillis(accessToken);

        Auth auth = authRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                auth.getPassword()
        )) {
            throw new AppException(ErrorCode.OLD_PASSWORD_INCORRECT);
        }

        // Update password
        auth.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // Invalidate all old access tokens
        auth.setAccessTokenVersion(auth.getAccessTokenVersion() + 1);

        authRepository.save(auth);

        // Blacklist current token
        redisSessionService.blacklistToken(jti, ttl);

        // Remove all refresh tokens
        refreshTokenRepository.deleteAll(
                refreshTokenRepository.findByAuthIdOrderByCreatedAtAsc(userId)
        );
    }
}