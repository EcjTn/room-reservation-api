package com.ecjtaneo.hotel_management_system.room;

import com.ecjtaneo.hotel_management_system.room.dto.RoomPublicResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<RoomPublicResponseDto> showRooms(@RequestParam(name = "cursor", required = false) Long cursor) {
        if(cursor == null) return roomService.showRooms();
        return roomService.showRoomsAfter(cursor);
    }

}
