package com.nnt.hotelbooking.hotel.dto.response;

import com.nnt.hotelbooking.hotel.constants.AvailabilityStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class RoomAvailabilityResponse {
    private LocalDate date;
    private AvailabilityStatus status;
    private BigDecimal price;
}
