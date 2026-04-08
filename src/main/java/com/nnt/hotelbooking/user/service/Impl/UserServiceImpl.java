package com.nnt.hotelbooking.user.service.Impl;

import com.nnt.hotelbooking.auth.event.UserRegisteredEvent;
import com.nnt.hotelbooking.common.exception.AppException;
import com.nnt.hotelbooking.common.exception.ErrorCode;
import com.nnt.hotelbooking.user.dto.request.UpdateProfileRequest;
import com.nnt.hotelbooking.user.dto.response.UserProfileResponse;
import com.nnt.hotelbooking.user.model.UserProfiles;
import com.nnt.hotelbooking.user.repository.UserProfilesRepository;
import com.nnt.hotelbooking.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-PROFILE-SERVICE")
public class UserServiceImpl implements UserService {

    private final UserProfilesRepository repository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createProfileFromRegisterEvent(UserRegisteredEvent event) {

        log.info("[CREATE-PROFILE] Request | userId={}",
                event.getAuthId());

        UserProfiles profile = UserProfiles.builder()
                .userProfileId(event.getAuthId())
                .authId(event.getAuthId())
                .fullName(event.getFullName())
                .dateOfBirth(event.getDateOfBirth())
                .gender(event.getGender())
                .nationality(event.getNationality())
                .phoneNumber(event.getPhoneNumber())
                .build();

        repository.saveAndFlush(profile);

        log.info("User profile saved successfully authId={}", event.getAuthId());
    }
    @Override
    public UserProfileResponse getProfile(Long authId) {

        log.info("[GET-PROFILE] Request | userId={}",
                authId);

        UserProfiles profile = repository.findById(authId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return mapToResponse(profile);
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfile(Long authId, UpdateProfileRequest request) {

        log.info("[UPDATE-PROFILE] Request | userId={}",
                authId);

        UserProfiles profile = repository.findById(authId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        profile.setFullName(request.getFullName());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setGender(request.getGender());
        profile.setNationality(request.getNationality());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setUpdatedAt(LocalDateTime.now());

        UserProfiles updated = repository.save(profile);

        return mapToResponse(updated);
    }

    private UserProfileResponse mapToResponse(UserProfiles profile) {
        return UserProfileResponse.builder()
                .fullName(profile.getFullName())
                .dateOfBirth(profile.getDateOfBirth())
                .gender(profile.getGender())
                .nationality(profile.getNationality())
                .phoneNumber(profile.getPhoneNumber())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

}