package com.nnt.hotelbooking.user.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String nationality;
    private String phoneNumber;
}
