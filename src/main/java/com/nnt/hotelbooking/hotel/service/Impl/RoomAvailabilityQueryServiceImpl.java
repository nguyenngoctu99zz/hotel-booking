package com.nnt.hotelbooking.hotel.service.Impl;

import com.nnt.hotelbooking.hotel.model.RoomAvailability;
import com.nnt.hotelbooking.hotel.repository.RoomAvailabilityRepository;
import com.nnt.hotelbooking.hotel.service.RoomAvailabilityQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomAvailabilityQueryServiceImpl implements RoomAvailabilityQueryService {

    private final RoomAvailabilityRepository roomAvailabilityRepository;

    @Override
    public List<RoomAvailability> findSlotsForUpdate(
            Long roomId,
            LocalDate checkin,
            LocalDate checkout
    ) {
        return roomAvailabilityRepository.findForUpdate(roomId, checkin, checkout);
    }

    @Override
    public void saveAll(List<RoomAvailability> slots) {
        roomAvailabilityRepository.saveAll(slots);
    }
}
