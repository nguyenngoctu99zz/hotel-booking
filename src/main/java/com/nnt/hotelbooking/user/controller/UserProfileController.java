package com.nnt.hotelbooking.user.controller;

import com.nnt.hotelbooking.common.response.ApiResponse;
import com.nnt.hotelbooking.user.dto.request.UpdateProfileRequest;
import com.nnt.hotelbooking.user.dto.response.UserProfileResponse;
import com.nnt.hotelbooking.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j(topic = "USER-PROFILE-CONTROLLER")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    @GetMapping("/{authId}")
    public ApiResponse<UserProfileResponse> getProfile(@PathVariable Long authId) {

        log.info("[GET-PROFILE][API][REQUEST] user-profile-id={}",
                authId);

        UserProfileResponse response = userService.getProfile(authId);

        return ApiResponse.<UserProfileResponse>builder()
                .code(200)
                .message("Get user profile successfully")
                .result(response)
                .build();
    }

    @PutMapping("/{authId}")
    public ApiResponse<UserProfileResponse> updateProfile(
            @PathVariable Long authId,
            @RequestBody UpdateProfileRequest request
    ) {
        log.info("[UPDATE-PROFILE][API][REQUEST] Update profile id={}",
                authId);

        UserProfileResponse response =
                userService.updateProfile(authId, request);

        return ApiResponse.<UserProfileResponse>builder()
                .code(200)
                .message("Update user profile successfully")
                .result(response)
                .build();
    }
}