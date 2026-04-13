package com.nnt.hotelbooking.hotel.repository;

import com.nnt.hotelbooking.hotel.dto.RoomDetailProjection;
import com.nnt.hotelbooking.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("""
        SELECT
            r.roomId AS roomId,
            r.hotelId AS hotelId,
            r.roomNumber AS roomNumber,
            r.floorNo AS floorNo,
            rt.roomTypeId AS roomTypeId,
            rt.name AS roomTypeName,
            rt.maxAdult AS maxAdult,
            rt.maxChild AS maxChild,
            rt.bedType AS bedType,
            rt.roomSize AS roomSize,
            rt.basePrice AS basePrice,
            rt.description AS description
        FROM Room r
        JOIN RoomType rt
            ON r.roomTypeId = rt.roomTypeId
        WHERE r.roomId = :roomId
    """)
    Optional<RoomDetailProjection> findRoomDetailById(Long roomId);
}