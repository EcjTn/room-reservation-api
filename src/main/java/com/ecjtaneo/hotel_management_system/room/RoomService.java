package com.ecjtaneo.hotel_management_system.room;

import com.ecjtaneo.hotel_management_system.common.dto.MessageResponseDto;
import com.ecjtaneo.hotel_management_system.common.exception.ResourceConflictException;
import com.ecjtaneo.hotel_management_system.common.exception.ResourceNotFoundException;
import com.ecjtaneo.hotel_management_system.room.dto.RoomCreationDto;
import com.ecjtaneo.hotel_management_system.room.dto.RoomPublicResponseDto;
import com.ecjtaneo.hotel_management_system.room.dto.RoomUpdateDto;
import com.ecjtaneo.hotel_management_system.room.model.Room;
import com.ecjtaneo.hotel_management_system.room.mapper.RoomMapper;
import com.ecjtaneo.hotel_management_system.room.model.RoomStatus;
import com.ecjtaneo.hotel_management_system.room.model.RoomType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
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

    public List<RoomPublicResponseDto> showRoomsBefore(Long lastSeenId) {
        return roomMapper.toDtoList(roomRepository.findTop10ByIdLessThanOrderByIdDesc(lastSeenId));
    }

    public List<RoomPublicResponseDto> showRoomsByFilters(RoomStatus status, RoomType type) {
        return roomMapper.toDtoList(roomRepository.findTop10ByStatusAndType(status, type));
    }

    public List<RoomPublicResponseDto> showRoomsByFiltersBefore(Long lastSeenId, RoomStatus status, RoomType type) {
        return roomMapper.toDtoList(roomRepository.findTop10ByStatusAndTypeAndIdLessThanOrderByIdDesc(lastSeenId, status, type));
    }

    public RoomPublicResponseDto findByRoomNumber(String roomNumber) {
        return roomMapper.toDtoSingle(roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found.")));
    }

    @Transactional
    public MessageResponseDto deleteByRoomNumber(String roomNumber) {
        roomRepository.deleteByRoomNumber(roomNumber);
        return new MessageResponseDto("Room successfully deleted.");
    }

    @Transactional
    public MessageResponseDto updateRoom(String roomNumber, RoomUpdateDto roomUpdateDto){
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        room.setStatus(roomUpdateDto.status());
        room.setType(roomUpdateDto.type());
        room.setPrice_per_night(roomUpdateDto.price_per_night());

        return new MessageResponseDto("Room successfully updated.");
    }

}
