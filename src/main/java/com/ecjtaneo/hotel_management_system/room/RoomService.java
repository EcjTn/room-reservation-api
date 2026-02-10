package com.ecjtaneo.hotel_management_system.room;

import com.ecjtaneo.hotel_management_system.common.dto.MessageResponseDto;
import com.ecjtaneo.hotel_management_system.common.exception.ResourceConflictException;
import com.ecjtaneo.hotel_management_system.common.exception.ResourceNotFoundException;
import com.ecjtaneo.hotel_management_system.room.dto.RoomCreationDto;
import com.ecjtaneo.hotel_management_system.room.dto.RoomPublicResponseDto;
import com.ecjtaneo.hotel_management_system.room.model.Room;
import com.ecjtaneo.hotel_management_system.user.mapper.RoomMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    public MessageResponseDto createNewRoom(RoomCreationDto roomCreationDto) {
        if(roomRepository.existsByRoomNumber(roomCreationDto.roomNumber())) throw new ResourceConflictException("Room already exists");
        Room room = roomMapper.toEntity(roomCreationDto);
        roomRepository.save(room);

        return new MessageResponseDto("Room successfully created.");
    }

    public List<RoomPublicResponseDto> showRooms() {
        return roomMapper.toDtoList(roomRepository.findTop10ByOrderByIdDesc());
    }

    public List<RoomPublicResponseDto> showRoomsAfter(Long lastSeenId) {
        return roomMapper.toDtoList(roomRepository.findTop10ByIdLessThanOrderByIdDesc(lastSeenId));
    }

    public Room findByRoomNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }

    @Transactional
    public void deleteByRoomNumber(String roomNumber) {
        roomRepository.deleteByRoomNumber(roomNumber);
    }

}
