package com.nnt.hotelbooking.hotel.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class RoomDetailResponse implements Serializable {

    private Long roomId;
    private Long hotelId;
    private String roomNumber;
    private Integer floorNo;
    private Long roomTypeId;
    private String roomTypeName;
    private Integer maxAdult;
    private Integer maxChild;
    private String bedType;
    private BigDecimal roomSize;
    private BigDecimal basePrice;
    private String description;
}
