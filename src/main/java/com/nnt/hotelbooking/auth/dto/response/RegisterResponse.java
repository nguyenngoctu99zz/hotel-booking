package com.nnt.hotelbooking.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private Long userId;
    private String username;
    private String email;
}