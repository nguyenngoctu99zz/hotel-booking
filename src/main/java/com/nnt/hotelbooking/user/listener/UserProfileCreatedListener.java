package com.nnt.hotelbooking.user.listener;


import com.nnt.hotelbooking.auth.event.UserRegisteredEvent;
import com.nnt.hotelbooking.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "USER-PROFILE-LISTENER")
public class UserProfileCreatedListener {

    private final UserService userService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserRegisteredEvent event) {

        log.info("[USER-PROFILE] Listener | UserRegisteredEvent authId={}", event.getAuthId());

        userService.createProfileFromRegisterEvent(event);
    }
}