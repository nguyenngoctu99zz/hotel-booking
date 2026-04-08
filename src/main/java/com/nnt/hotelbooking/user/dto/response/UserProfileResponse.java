package com.nnt.hotelbooking.user.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class UserProfileResponse {
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String nationality;
    private String phoneNumber;
    private LocalDateTime updatedAt;
}
