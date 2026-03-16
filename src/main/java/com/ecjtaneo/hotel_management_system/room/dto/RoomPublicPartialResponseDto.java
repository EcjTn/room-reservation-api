package com.ecjtaneo.hotel_management_system.room.dto;

import com.ecjtaneo.hotel_management_system.room.model.RoomType;

import java.math.BigDecimal;

public record RoomPublicPartialResponseDto(
        Long id,
        String roomNumber,
        RoomType type
) {}
