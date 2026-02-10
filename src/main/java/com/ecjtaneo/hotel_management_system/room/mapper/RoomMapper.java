package com.ecjtaneo.hotel_management_system.room.mapper;

import com.ecjtaneo.hotel_management_system.room.dto.RoomCreationDto;
import com.ecjtaneo.hotel_management_system.room.dto.RoomPublicResponseDto;
import com.ecjtaneo.hotel_management_system.room.model.Room;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    public RoomPublicResponseDto toDtoSingle(Room room);
    public List<RoomPublicResponseDto> toDtoList(List<Room> room);
    public Room toEntity(RoomCreationDto roomCreationDto);
}
