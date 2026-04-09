package com.nnt.hotelbooking.booking.controller;

import com.nnt.hotelbooking.booking.dto.request.CreateBookingRequest;
import com.nnt.hotelbooking.booking.dto.response.BookingResponse;
import com.nnt.hotelbooking.booking.service.BookingService;
import com.nnt.hotelbooking.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ApiResponse<BookingResponse> createBooking(
            @Valid @RequestBody CreateBookingRequest request
    ) {
        return ApiResponse.<BookingResponse>builder()
                .code(200)
                .message("Booking created successfully")
                .result(bookingService.createBooking(request))
                .build();
    }
}