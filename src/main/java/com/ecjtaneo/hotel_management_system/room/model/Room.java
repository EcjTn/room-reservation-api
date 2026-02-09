package com.ecjtaneo.hotel_management_system.room.model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rooms")
public class Room {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String roomNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    RoomStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    RoomType type;

    @Column(nullable = false)
    BigDecimal price_per_night;

    LocalDateTime created_at = LocalDateTime.now();
}
