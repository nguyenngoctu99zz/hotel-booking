package com.nnt.hotelbooking.hotel.dto;

import java.math.BigDecimal;

public interface RoomDetailProjection {
    Long getRoomId();
    Long getHotelId();
    String getRoomNumber();
    Integer getFloorNo();

    Long getRoomTypeId();
    String getRoomTypeName();
    Integer getMaxAdult();
    Integer getMaxChild();
    String getBedType();
    BigDecimal getRoomSize();
    BigDecimal getBasePrice();
    String getDescription();
}
