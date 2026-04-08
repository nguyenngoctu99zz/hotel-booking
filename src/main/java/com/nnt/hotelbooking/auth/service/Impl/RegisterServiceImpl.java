package com.nnt.hotelbooking.auth.service.Impl;

import com.nnt.hotelbooking.auth.dto.request.RegisterRequest;
import com.nnt.hotelbooking.auth.dto.response.RegisterResponse;
import com.nnt.hotelbooking.auth.event.UserRegisteredEvent;
import com.nnt.hotelbooking.auth.model.Auth;
import com.nnt.hotelbooking.auth.repository.AuthRepository;
import com.nnt.hotelbooking.auth.service.RegisterService;
import com.nnt.hotelbooking.common.exception.AppException;
import com.nnt.hotelbooking.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j(topic = "REGISTER-SERVICE")
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public RegisterResponse register(RegisterRequest request) {
        log.info("[REGISTER] Request | username={}",
                request.getUsername());

        if (authRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTS);
        }

        if (authRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }

        Auth auth = Auth.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountStatus("ACTIVE")
                .build();

        Auth saved = authRepository.save(auth);

        eventPublisher.publishEvent(
                new UserRegisteredEvent(
                        saved.getId(),
                        request.getFullName(),
                        request.getDateOfBirth(),
                        request.getGender(),
                        request.getNationality(),
                        request.getPhoneNumber()
                )
        );

        return RegisterResponse.builder()
                .userId(saved.getId())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .build();
    }


}
