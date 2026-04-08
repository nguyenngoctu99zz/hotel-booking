package com.nnt.hotelbooking.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username must not be blank")
    private String username;

    @Email
    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotBlank(message = "Password must not be blank")
    private String password;

    // profile fields
    @NotBlank(message = "Full name must not be blank")
    private String fullName;

    private LocalDate dateOfBirth;

    private String gender;

    private String nationality;

    private String phoneNumber;
}