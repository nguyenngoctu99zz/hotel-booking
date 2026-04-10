package com.nnt.hotelbooking.auth.utils;

import com.nnt.hotelbooking.auth.service.RedisSessionService;
import com.nnt.hotelbooking.common.currentUser.CustomUserPrincipal;
import com.nnt.hotelbooking.common.exception.AppException;
import com.nnt.hotelbooking.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import com.nnt.hotelbooking.auth.model.Auth;
import com.nnt.hotelbooking.auth.repository.AuthRepository;
import io.jsonwebtoken.JwtException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisSessionService redisSessionService;
    private final AuthRepository authRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // Không có token -> cho đi tiếp
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = header.substring(7);

            // 1) verify JWT + expiration
            Claims claims = jwtUtil.extractClaims(token);

            String username = claims.getSubject();
            String jti = claims.getId();
            Long userId = claims.get("userId", Long.class);
            Integer tokenVersion = claims.get("version", Integer.class);

            //2) blacklist check
            if (redisSessionService.isBlacklisted(jti)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token blacklisted");
                return;
            }

            // 3) active session check
            if (!redisSessionService.isActive(jti)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token not active");
                return;
            }

            // 4) token version check
            Auth auth = authRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.TOKEN_VERSION_INVALID));

            if (!tokenVersion.equals(auth.getAccessTokenVersion())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token version invalid");
                return;
            }

            CustomUserPrincipal principal = new CustomUserPrincipal(
                    userId,
                    username,
                    Collections.emptyList()
            );

            // 5) set authenticated user
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            principal.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException | IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}