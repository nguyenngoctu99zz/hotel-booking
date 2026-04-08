package com.nnt.hotelbooking.hotel.service.Impl;

import com.nnt.hotelbooking.hotel.dto.RoomDetailProjection;
import com.nnt.hotelbooking.hotel.dto.response.RoomDetailResponse;
import com.nnt.hotelbooking.hotel.repository.RoomRepository;
import com.nnt.hotelbooking.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "ROOM-SERVICE")
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private static final String ROOM_DETAIL_KEY = "hb:hotel:room:detail:";
    private static final long TTL_MINUTES = 30;

    private final RoomRepository roomRepository;

    @Qualifier("hotelRedisTemplate")
    private final RedisTemplate<String, Object> hotelRedisTemplate;

    @Override
    public RoomDetailResponse getRoomDetail(Long roomId) {
        String cacheKey = ROOM_DETAIL_KEY + roomId;

        // READ FROM REDIS HASH
        Map<Object, Object> cached = hotelRedisTemplate.opsForHash().entries(cacheKey);

        if (!cached.isEmpty()) {
            log.info("ROOM HASH CACHE HIT: {}", cacheKey);

            return RoomDetailResponse.builder()
                    .roomId(Long.parseLong(cached.get("roomId").toString()))
                    .hotelId(Long.parseLong(cached.get("hotelId").toString()))
                    .roomNumber(cached.get("roomNumber").toString())
                    .floorNo(Integer.parseInt(cached.get("floorNo").toString()))
                    .roomTypeId(Long.parseLong(cached.get("roomTypeId").toString()))
                    .roomTypeName(cached.get("roomTypeName").toString())
                    .maxAdult(Integer.parseInt(cached.get("maxAdult").toString()))
                    .maxChild(Integer.parseInt(cached.get("maxChild").toString()))
                    .bedType(cached.get("bedType").toString())
                    .roomSize(new BigDecimal(cached.get("roomSize").toString()))
                    .basePrice(new BigDecimal(cached.get("basePrice").toString()))
                    .description(cached.get("description").toString())
                    .build();
        }

        // CACHE MISS -> DB
        log.info("ROOM HASH CACHE MISS: {}", cacheKey);

        RoomDetailProjection room = roomRepository.findRoomDetailById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        RoomDetailResponse response = RoomDetailResponse.builder()
                .roomId(room.getRoomId())
                .hotelId(room.getHotelId())
                .roomNumber(room.getRoomNumber())
                .floorNo(room.getFloorNo())
                .roomTypeId(room.getRoomTypeId())
                .roomTypeName(room.getRoomTypeName())
                .maxAdult(room.getMaxAdult())
                .maxChild(room.getMaxChild())
                .bedType(room.getBedType())
                .roomSize(room.getRoomSize())
                .basePrice(room.getBasePrice())
                .description(room.getDescription())
                .build();

        // SAVE TO REDIS HASH
        Map<String, String> roomHash = new HashMap<>();
        roomHash.put("roomId", response.getRoomId().toString());
        roomHash.put("hotelId", response.getHotelId().toString());
        roomHash.put("roomNumber", response.getRoomNumber());
        roomHash.put("floorNo", String.valueOf(response.getFloorNo()));
        roomHash.put("roomTypeId", response.getRoomTypeId().toString());
        roomHash.put("roomTypeName", response.getRoomTypeName());
        roomHash.put("maxAdult", response.getMaxAdult().toString());
        roomHash.put("maxChild", response.getMaxChild().toString());
        roomHash.put("bedType", response.getBedType());
        roomHash.put("roomSize", response.getRoomSize().toString());
        roomHash.put("basePrice", response.getBasePrice().toString());
        roomHash.put("description", response.getDescription());

        hotelRedisTemplate.opsForHash().putAll(cacheKey, roomHash);
        hotelRedisTemplate.expire(cacheKey, TTL_MINUTES, TimeUnit.MINUTES);

        log.info("ROOM HASH CACHE SAVED: {}", cacheKey);

        return response;
    }

    @Override
    public void evictRoomCache(Long roomId) {
        String cacheKey = ROOM_DETAIL_KEY + roomId;
        hotelRedisTemplate.delete(cacheKey);

        log.info("ROOM HASH CACHE EVICTED: {}", cacheKey);
    }
}