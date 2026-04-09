package com.nnt.hotelbooking.hotel.model;

import com.nnt.hotelbooking.hotel.constants.AvailabilityStatus;
import com.nnt.hotelbooking.hotel.converter.AvailabilityStatusConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_availability", catalog = "hotel_schema")
public class RoomAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_available_id")
    private Long roomAvailableId;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "available_date", nullable = false)
    private LocalDate availableDate;

    @Convert(converter = AvailabilityStatusConverter.class)
    @Column(name = "availability_status", nullable = false)
    private AvailabilityStatus availabilityStatus;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;
}
