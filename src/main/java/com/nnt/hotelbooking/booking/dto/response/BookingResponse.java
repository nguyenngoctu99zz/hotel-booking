package com.nnt.hotelbooking.booking.dto.response;


import com.nnt.hotelbooking.booking.constants.BookingStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class BookingResponse {
    private Long bookingId;
    private String bookingCode;
    private Long roomId;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Integer totalNights;
    private BigDecimal totalAmount;
    private BookingStatus bookingStatus;
}