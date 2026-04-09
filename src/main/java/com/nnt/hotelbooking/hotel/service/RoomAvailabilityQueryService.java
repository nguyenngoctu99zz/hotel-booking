package com.nnt.hotelbooking.hotel.service;

import com.nnt.hotelbooking.hotel.model.RoomAvailability;

import java.time.LocalDate;
import java.util.List;

public interface RoomAvailabilityQueryService {

    List<RoomAvailability> findSlotsForUpdate(
            Long roomId,
            LocalDate checkin,
            LocalDate checkout
    );

    void saveAll(List<RoomAvailability> slots);
}
