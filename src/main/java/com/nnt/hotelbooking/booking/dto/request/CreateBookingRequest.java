package com.nnt.hotelbooking.booking.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class CreateBookingRequest {

    @NotNull
    private Long roomId;

    @NotNull
    private LocalDate checkinDate;

    @NotNull
    private LocalDate checkoutDate;
}