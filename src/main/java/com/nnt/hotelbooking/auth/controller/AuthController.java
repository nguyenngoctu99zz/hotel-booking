package com.nnt.hotelbooking.auth.controller;

import com.nnt.hotelbooking.auth.dto.request.ChangePasswordRequest;
import com.nnt.hotelbooking.auth.dto.request.LoginRequest;
import com.nnt.hotelbooking.auth.dto.request.RefreshTokenRequest;
import com.nnt.hotelbooking.auth.dto.request.RegisterRequest;
import com.nnt.hotelbooking.auth.dto.response.LoginResponse;
import com.nnt.hotelbooking.auth.dto.response.RefreshTokenResponse;
import com.nnt.hotelbooking.auth.dto.response.RegisterResponse;
import com.nnt.hotelbooking.auth.service.AuthService;
import com.nnt.hotelbooking.auth.service.ChangePasswordService;
import com.nnt.hotelbooking.auth.service.RegisterService;
import com.nnt.hotelbooking.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j(topic = "AUTH-CONTROLLER")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ChangePasswordService changePasswordService;
    private final RegisterService registerService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        log.info("[LOGIN][API][REQUEST] username={}",
                request.getUsername());

        return ApiResponse.<LoginResponse>builder()
                .code(200)
                .message("Login success")
                .result(authService.login(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        log.info("[LOGOUT][API][REQUEST] Request to log out");

        String token = request.getHeader("Authorization").replace("Bearer ", "");
        authService.logout(token);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Logout success")
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(
            @RequestBody RegisterRequest request
    ) {

        log.info("[REGISTER][API][REQUEST] username={}",
                request.getUsername());

        return ApiResponse.<RegisterResponse>builder()
                .code(200)
                .message("Register success")
                .result(registerService.register(request))
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<RefreshTokenResponse> refresh(
            @RequestBody RefreshTokenRequest request
    ) {
        log.info("[REFRESH-TOKEN][API][REQUEST]");

        return ApiResponse.<RefreshTokenResponse>builder()
                .code(200)
                .message("Refresh success")
                .result(authService.refreshToken(request))
                .build();
    }

    @PostMapping("/change-password")
    public ApiResponse<Void> changePassword(
            HttpServletRequest request,
            @RequestBody ChangePasswordRequest body
    ) {

        log.info("[CHANGE-PASSWORD][API][REQUEST] Password changing");

        String token = request.getHeader("Authorization").replace("Bearer ", "");

        changePasswordService.changePassword(token, body);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Password changed successfully")
                .build();
    }

    @GetMapping("/check-token")
    public ApiResponse<String> checkToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");

        return ApiResponse.<String>builder()
                .code(200)
                .message("Token checked")
                .result(authService.checkToken(token))
                .build();
    }


}
