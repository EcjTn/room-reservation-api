package com.ecjtaneo.hotel_management_system.room;

import com.ecjtaneo.hotel_management_system.common.exception.ResourceNotFoundException;
import com.ecjtaneo.hotel_management_system.room.model.Room;
import org.springframework.transaction.annotation.Transactional;

public class RoomService {
    private RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
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
