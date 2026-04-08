package com.nnt.hotelbooking.hotel.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_types", catalog = "hotel_schema")
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    private Long roomTypeId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "max_adult", nullable = false)
    private Integer maxAdult;

    @Column(name = "max_child", nullable = false)
    private Integer maxChild;

    @Column(name = "bed_type", length = 100)
    private String bedType;

    @Column(name = "room_size", precision = 10, scale = 2)
    private BigDecimal roomSize;

    @Column(name = "base_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
