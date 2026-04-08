package com.nnt.hotelbooking.auth.service;

import com.nnt.hotelbooking.auth.dto.request.LoginRequest;
import com.nnt.hotelbooking.auth.dto.request.RefreshTokenRequest;
import com.nnt.hotelbooking.auth.dto.response.LoginResponse;
import com.nnt.hotelbooking.auth.dto.response.RefreshTokenResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void logout(String accessToken);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    String checkToken(String accessToken);
}
