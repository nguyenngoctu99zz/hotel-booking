package com.nnt.hotelbooking.hotel.constants;


import lombok.Getter;

@Getter
public enum AvailabilityStatus {
    AVAILABLE(1),
    BOOKING_IN_PROGRESS(2),
    BOOKED(3),
    MAINTENANCE(4);

    private final int code;

    AvailabilityStatus(int code) {
        this.code = code;
    }

    public static AvailabilityStatus fromCode(Integer code) {
        for (AvailabilityStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid AvailabilityStatus code: " + code);
    }
}
