package com.nnt.hotelbooking.auth.service;

import com.nnt.hotelbooking.auth.dto.request.ChangePasswordRequest;

public interface ChangePasswordService {

    void changePassword(String accessToken, ChangePasswordRequest request);
}
