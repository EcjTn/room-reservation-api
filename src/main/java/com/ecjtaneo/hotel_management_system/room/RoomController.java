package com.ecjtaneo.hotel_management_system.room;

import com.ecjtaneo.hotel_management_system.common.dto.MessageResponseDto;
import com.ecjtaneo.hotel_management_system.room.dto.RoomCreationDto;
import com.ecjtaneo.hotel_management_system.room.dto.RoomPublicResponseDto;
import com.ecjtaneo.hotel_management_system.room.dto.RoomUpdateDto;
import com.ecjtaneo.hotel_management_system.room.model.RoomStatus;
import com.ecjtaneo.hotel_management_system.room.model.RoomType;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/{roomNumber}")
    public RoomPublicResponseDto showRoomByRoomNumber(@PathVariable String roomNumber) {
        return roomService.findByRoomNumber(roomNumber);
    }

    @GetMapping
    public List<RoomPublicResponseDto> showRooms(@RequestParam(name = "cursor", required = false) Long cursor) {
        if(cursor == null) return roomService.showRooms();
        return roomService.showRoomsBefore(cursor);
    }

    @GetMapping("/filters")
    public List<RoomPublicResponseDto> showRoomsWithFilter(
            @RequestParam(name = "status", required = false, defaultValue = "AVAILABLE") RoomStatus status,
            @RequestParam(name = "type", required = false, defaultValue = "SINGLE") RoomType type,
            @RequestParam(name = "cursor", required = false) Long cursor
    ) {
        if(cursor == null) return roomService.showRoomsByFilters(status, type);
        return roomService.showRoomsByFiltersBefore(cursor, status, type);
    }

    @PostMapping
    public MessageResponseDto createNewRoom(@RequestBody @Valid RoomCreationDto roomCreationDto) {
        return roomService.createNewRoom(roomCreationDto);
    }

    @DeleteMapping("/{roomNumber}")
    public MessageResponseDto deleteRoom(@PathVariable String roomNumber) {
        return roomService.deleteByRoomNumber(roomNumber);
    }

    @PutMapping("/{roomNumber}")
    public MessageResponseDto updateRoom(@PathVariable String roomNumber, @RequestBody @Valid RoomUpdateDto roomUpdateDto) {
        return roomService.updateRoom(roomNumber, roomUpdateDto);
    }

}
