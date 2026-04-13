package com.nnt.hotelbooking.auth.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserRegisteredEvent {

    private final Long authId;

    private final String fullName;
    private final LocalDate dateOfBirth;
    private final String gender;
    private final String nationality;
    private final String phoneNumber;
}
