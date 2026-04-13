package com.nnt.hotelbooking.auth.service;

import com.nnt.hotelbooking.auth.dto.request.RegisterRequest;
import com.nnt.hotelbooking.auth.dto.response.RegisterResponse;

public interface RegisterService {
    RegisterResponse register(RegisterRequest request);

}
