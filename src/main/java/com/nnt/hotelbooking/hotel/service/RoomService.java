package com.nnt.hotelbooking.hotel.service;

import com.nnt.hotelbooking.hotel.dto.response.RoomDetailResponse;


public interface RoomService {
    RoomDetailResponse getRoomDetail(Long roomId);
    void evictRoomCache(Long roomId);
}