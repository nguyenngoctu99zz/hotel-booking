package com.nnt.hotelbooking.hotel.service;

import com.nnt.hotelbooking.hotel.dto.response.RoomAvailabilityResponse;

import java.time.LocalDate;
import java.util.List;

public interface RoomAvailabilityService {
    List<RoomAvailabilityResponse> getRoomAvailability(
            Long roomId,
            LocalDate fromDate,
            LocalDate toDate
    );

}
