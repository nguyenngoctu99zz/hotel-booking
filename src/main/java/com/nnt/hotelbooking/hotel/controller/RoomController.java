package com.nnt.hotelbooking.hotel.controller;

import com.nnt.hotelbooking.hotel.dto.response.RoomDetailResponse;
import com.nnt.hotelbooking.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "ROOM-CONTROLLER")
@RestController
@RequestMapping("rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/{roomId}")
    public RoomDetailResponse getRoomDetail(@PathVariable Long roomId) {
        return roomService.getRoomDetail(roomId);
    }

    @DeleteMapping("/cache/{roomId}")
    public String clearCache(@PathVariable Long roomId) {
        roomService.evictRoomCache(roomId);
        return "Room cache cleared";
    }
}