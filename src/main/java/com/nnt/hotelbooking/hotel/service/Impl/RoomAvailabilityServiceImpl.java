package com.nnt.hotelbooking.hotel.service.Impl;

import com.nnt.hotelbooking.common.exception.AppException;
import com.nnt.hotelbooking.common.exception.ErrorCode;
import com.nnt.hotelbooking.hotel.dto.response.RoomAvailabilityResponse;
import com.nnt.hotelbooking.hotel.model.RoomAvailability;
import com.nnt.hotelbooking.hotel.repository.RoomAvailabilityRepository;
import com.nnt.hotelbooking.hotel.service.RoomAvailabilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j(topic = "ROOM-AVAILABILITY-SERVICE")
@Service
@RequiredArgsConstructor
public class RoomAvailabilityServiceImpl implements RoomAvailabilityService {

    private final RoomAvailabilityRepository roomAvailabilityRepository;

    @Override
    public List<RoomAvailabilityResponse> getRoomAvailability(
            Long roomId,
            LocalDate fromDate,
            LocalDate toDate
    ) {

        log.info("[ROOM-AVAILABILITY] Request | roomId: {}", roomId);

        if (fromDate.isAfter(toDate)) {
            throw new AppException(ErrorCode.INVALID_BOOKING_DATE);
        }

        List<RoomAvailability> availabilities =
                roomAvailabilityRepository.findByRoomIdAndDateRange(
                        roomId,
                        fromDate,
                        toDate
                );

        if (availabilities.isEmpty()) {
            throw new AppException(ErrorCode.ROOM_AVAILABILITY_NOT_FOUND);
        }

        return availabilities.stream()
                .map(item -> RoomAvailabilityResponse.builder()
                        .date(item.getAvailableDate())
                        .status(item.getAvailabilityStatus())
                        .price(item.getPrice())
                        .build())
                .toList();
    }
}
