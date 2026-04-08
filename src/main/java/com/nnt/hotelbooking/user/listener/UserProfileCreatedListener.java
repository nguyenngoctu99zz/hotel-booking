package com.nnt.hotelbooking.user.listener;


import com.nnt.hotelbooking.auth.event.UserRegisteredEvent;
import com.nnt.hotelbooking.user.model.UserProfiles;
import com.nnt.hotelbooking.user.repository.UserProfilesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserProfileCreatedListener {

    private final UserProfilesRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserRegisteredEvent event) {

        log.info("Received UserRegisteredEvent for authId={}", event.getAuthId());

        try {
            UserProfiles profile = UserProfiles.builder()
                    .userProfileId(event.getAuthId())
                    .authId(event.getAuthId())
                    .fullName(event.getFullName())
                    .dateOfBirth(event.getDateOfBirth())
                    .gender(event.getGender())
                    .nationality(event.getNationality())
                    .phoneNumber(event.getPhoneNumber())
                    .build();

            log.info("Saving user profile for authId={}", event.getAuthId());

            repository.saveAndFlush(profile);

            log.info("User profile created successfully for authId={}", event.getAuthId());

        } catch (Exception e) {
            log.error("Failed to create user profile for authId={}",
                    event.getAuthId(), e);
            throw e;
        }
    }
}