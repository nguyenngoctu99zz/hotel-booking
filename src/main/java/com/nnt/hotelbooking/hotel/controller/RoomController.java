package com.nnt.hotelbooking.hotel.controller;

import com.nnt.hotelbooking.common.exception.ErrorCode;
import com.nnt.hotelbooking.common.response.ApiResponse;
import com.nnt.hotelbooking.hotel.dto.response.RoomAvailabilityResponse;
import com.nnt.hotelbooking.hotel.dto.response.RoomDetailResponse;
import com.nnt.hotelbooking.hotel.service.RoomAvailabilityService;
import com.nnt.hotelbooking.hotel.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Room", description = "Room management and availability APIs")
@Slf4j(topic = "ROOM-CONTROLLER")
@RestController
@RequestMapping("rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomAvailabilityService roomAvailabilityService;

    @Operation(
            summary = "Get room detail",
            description = "Retrieve room detail information by room ID"
    )
    @GetMapping("/{roomId}")
    public ApiResponse<RoomDetailResponse> getRoomDetail(
            @PathVariable Long roomId
    ) {
        log.info("[ROOM-DETAIL][API][REQUEST] roomId={}", roomId);

        return ApiResponse.<RoomDetailResponse>builder()
                .code(ErrorCode.SUCCESS.getCode())
                .message("Get room detail successfully")
                .result(roomService.getRoomDetail(roomId))
                .build();
    }

    @Operation(
            summary = "Clear room cache",
            description = "Evict cached room detail from Redis"
    )
    @DeleteMapping("/cache/{roomId}")
    public ApiResponse<String> clearCache(
            @PathVariable Long roomId
    ) {
        log.info("[ROOM-CACHE][API][REQUEST] Clear cache | roomId={}", roomId);

        roomService.evictRoomCache(roomId);

        return ApiResponse.<String>builder()
                .code(ErrorCode.SUCCESS.getCode())
                .message("Room cache cleared successfully")
                .result("OK")
                .build();
    }

    @Operation(
            summary = "Check room availability",
            description = "Get room availability between check-in and check-out dates"
    )
    @GetMapping("/{roomId}/availability")
    public ApiResponse<List<RoomAvailabilityResponse>> getAvailability(
            @PathVariable Long roomId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    ) {
        log.info("[ROOM-AVAILABILITY][API][REQUEST] roomId={}, from={}, to={}",
                roomId, from, to);

        return ApiResponse.<List<RoomAvailabilityResponse>>builder()
                .code(ErrorCode.SUCCESS.getCode())
                .message("Get room availability successfully")
                .result(roomAvailabilityService.getRoomAvailability(roomId, from, to))
                .build();
    }

}