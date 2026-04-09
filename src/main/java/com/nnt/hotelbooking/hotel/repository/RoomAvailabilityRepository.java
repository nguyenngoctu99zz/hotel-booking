package com.nnt.hotelbooking.hotel.repository;

import com.nnt.hotelbooking.hotel.model.RoomAvailability;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT ra
        FROM RoomAvailability ra
        WHERE ra.roomId = :roomId
          AND ra.availableDate >= :checkin
          AND ra.availableDate <= :checkout
    """)
    List<RoomAvailability> findForUpdate(
            @Param("roomId") Long roomId,
            @Param("checkin") LocalDate checkin,
            @Param("checkout") LocalDate checkout
    );
}
