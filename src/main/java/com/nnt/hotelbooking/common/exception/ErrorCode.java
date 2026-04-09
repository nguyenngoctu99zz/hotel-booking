package com.nnt.hotelbooking.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // ===== COMMON =====
    SUCCESS(200, "Thành công", HttpStatus.OK),
    FAILED(500, "Thất bại", HttpStatus.BAD_REQUEST),
    VALIDATION_ERROR(400, "Validation error", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_FORMAT(10001, "Thông tin không đúng định dạng", HttpStatus.BAD_REQUEST),

    // AUTH 1000
    ACCOUNT_NOT_FOUND(1001, "Account not found", HttpStatus.NOT_FOUND),
    INVALID_USERNAME_PASSWORD(1002, "Invalid username or password", HttpStatus.UNAUTHORIZED),
    USERNAME_EXISTS(1003, "Username already exists", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS(1004, "Email already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1005, "User not found", HttpStatus.NOT_FOUND),

    // TOKEN 2000
    INVALID_TOKEN(2001, "Invalid token", HttpStatus.UNAUTHORIZED),
    TOKEN_BLACKLISTED(2002, "Token blacklisted", HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_ACTIVE(2003, "Token not active", HttpStatus.UNAUTHORIZED),
    TOKEN_VERSION_INVALID(2004, "Token version invalid", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID(2005, "Refresh token invalid", HttpStatus.UNAUTHORIZED),

    // PASSWORD 3000
    OLD_PASSWORD_INCORRECT(3001, "Old password incorrect", HttpStatus.BAD_REQUEST),

    // BOOKING 4000
    ROOM_NOT_AVAILABLE(4001, "Room is not available", HttpStatus.CONFLICT),
    ROOM_AVAILABILITY_NOT_FOUND(4002, "Room availability not found", HttpStatus.NOT_FOUND),
    INVALID_BOOKING_DATE(4003, "Invalid booking date", HttpStatus.BAD_REQUEST),
    BOOKING_NOT_FOUND(4004, "Booking not found", HttpStatus.NOT_FOUND),
    ROOM_NOT_FOUND(4001, "Room not found", HttpStatus.NOT_FOUND);



    private final Integer code;
    private final String message;
    private final HttpStatus statusCode;
}
