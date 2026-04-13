package com.nnt.hotelbooking.auth.constants;

public enum AccountStatus {
    ACTIVE(0),
    INACTIVE(1);

    private final int value;

    AccountStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AccountStatus fromValue(Integer value) {
        if (value == null) {
            return ACTIVE;
        }

        for (AccountStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }

        throw new IllegalArgumentException("Invalid account status: " + value);
    }
}