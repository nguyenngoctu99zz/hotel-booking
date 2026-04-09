package com.nnt.hotelbooking.booking.service;

import com.nnt.hotelbooking.booking.dto.request.CreateBookingRequest;
import com.nnt.hotelbooking.booking.dto.response.BookingResponse;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest request);
}
