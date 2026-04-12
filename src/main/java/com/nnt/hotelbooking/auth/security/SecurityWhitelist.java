package com.nnt.hotelbooking.auth.security;

public final class SecurityWhitelist {

    private SecurityWhitelist() {
    }

    public static final String[] PUBLIC_ENDPOINTS = {
            "/auth/login",
            "/auth/register",
            "/auth/refresh",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api-docs/**"
    };
}