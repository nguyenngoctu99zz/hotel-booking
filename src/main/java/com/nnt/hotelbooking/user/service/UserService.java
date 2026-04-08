package com.nnt.hotelbooking.user.service;

import com.nnt.hotelbooking.auth.event.UserRegisteredEvent;
import com.nnt.hotelbooking.user.dto.request.UpdateProfileRequest;
import com.nnt.hotelbooking.user.dto.response.UserProfileResponse;

public interface UserService {
    void createProfileFromRegisterEvent(UserRegisteredEvent event);

    UserProfileResponse getProfile(Long authId);

    UserProfileResponse updateProfile(Long authId, UpdateProfileRequest request);
}