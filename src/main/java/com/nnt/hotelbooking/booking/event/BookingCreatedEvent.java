package com.nnt.hotelbooking.booking.event;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreatedEvent {
    private Long bookingId;
    private Long roomId;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
}