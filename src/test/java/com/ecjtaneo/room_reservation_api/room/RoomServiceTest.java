package com.ecjtaneo.room_reservation_api.room;

import com.ecjtaneo.room_reservation_api.common.dto.MessageResponseDto;
import com.ecjtaneo.room_reservation_api.common.exception.ResourceConflictException;
import com.ecjtaneo.room_reservation_api.common.exception.ResourceNotFoundException;
import com.ecjtaneo.room_reservation_api.room.dto.RoomCreationDto;
import com.ecjtaneo.room_reservation_api.room.dto.RoomPublicResponseDto;
import com.ecjtaneo.room_reservation_api.room.dto.RoomUpdateDto;
import com.ecjtaneo.room_reservation_api.room.mapper.RoomMapper;
import com.ecjtaneo.room_reservation_api.room.model.Room;
import com.ecjtaneo.room_reservation_api.room.model.RoomStatus;
import com.ecjtaneo.room_reservation_api.room.model.RoomType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomService roomService;

    @Test
    void createNewRoom_shouldCreateRoomSuccessfully() {
        RoomCreationDto dto = new RoomCreationDto("101", RoomStatus.AVAILABLE, RoomType.SINGLE, BigDecimal.valueOf(100));
        Room room = new Room();
        when(roomRepository.existsByRoomNumber("101")).thenReturn(false);
        when(roomMapper.toEntity(dto)).thenReturn(room);

        MessageResponseDto result = roomService.createNewRoom(dto);

        assertEquals("Room successfully created.", result.message());
        verify(roomRepository).existsByRoomNumber("101");
        verify(roomMapper).toEntity(dto);
        verify(roomRepository).save(room);
    }

    @Test
    void createNewRoom_shouldThrowResourceConflictException() {
        RoomCreationDto dto = new RoomCreationDto("101", RoomStatus.AVAILABLE, RoomType.SINGLE, BigDecimal.valueOf(100));
        when(roomRepository.existsByRoomNumber("101")).thenReturn(true);

        ResourceConflictException exception = assertThrows(ResourceConflictException.class, () -> roomService.createNewRoom(dto));
        assertEquals("Room already exists", exception.getMessage());
    }

    @Test
    void findRoomByRoomNumber_shouldReturnRoom() {
        Room room = new Room();
        room.setRoomNumber("101");
        RoomPublicResponseDto dto = new RoomPublicResponseDto(1L, "101", RoomStatus.AVAILABLE, RoomType.SINGLE, BigDecimal.valueOf(100));
        when(roomRepository.findByRoomNumber("101")).thenReturn(Optional.of(room));
        when(roomMapper.toDtoSingle(room)).thenReturn(dto);

        RoomPublicResponseDto result = roomService.findRoomByRoomNumber("101");

        assertEquals(dto, result);
        verify(roomRepository).findByRoomNumber("101");
        verify(roomMapper).toDtoSingle(room);
    }

    @Test
    void findRoomByRoomNumber_shouldThrowResourceNotFoundException() {
        when(roomRepository.findByRoomNumber("101")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> roomService.findRoomByRoomNumber("101"));
        assertEquals("Room not found.", exception.getMessage());
    }

    @Test
    void deleteByRoomNumber_shouldDeleteRoom() {
        MessageResponseDto result = roomService.deleteByRoomNumber("101");

        assertEquals("Room successfully deleted.", result.message());
        verify(roomRepository).deleteByRoomNumber("101");
    }

    @Test
    void updateRoom_shouldUpdateRoomSuccessfully() {
        Room room = new Room();
        room.setRoomNumber("101");
        RoomUpdateDto dto = new RoomUpdateDto(RoomStatus.BOOKED, RoomType.DOUBLE, BigDecimal.valueOf(150));
        when(roomRepository.findByRoomNumber("101")).thenReturn(Optional.of(room));

        MessageResponseDto result = roomService.updateRoom("101", dto);

        assertEquals("Room successfully updated.", result.message());
        assertEquals(RoomStatus.BOOKED, room.getStatus());
        assertEquals(RoomType.DOUBLE, room.getType());
        assertEquals(BigDecimal.valueOf(150), room.getPricePerNight());
        verify(roomRepository).findByRoomNumber("101");
    }

    @Test
    void updateRoom_shouldThrowResourceNotFoundException() {
        RoomUpdateDto dto = new RoomUpdateDto(RoomStatus.BOOKED, RoomType.DOUBLE, BigDecimal.valueOf(150));
        when(roomRepository.findByRoomNumber("101")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> roomService.updateRoom("101", dto));
        assertEquals("Room not found", exception.getMessage());
    }

    @Test
    void setRoomBooked_shouldUpdateStatus() {
        when(roomRepository.updateStatusByRoomNumber("101", RoomStatus.BOOKED)).thenReturn(1);

        int result = roomService.setRoomBooked("101");

        assertEquals(1, result);
        verify(roomRepository).updateStatusByRoomNumber("101", RoomStatus.BOOKED);
    }

    @Test
    void setRoomAvailable_shouldUpdateStatus() {
        when(roomRepository.updateStatusByRoomNumber("101", RoomStatus.AVAILABLE)).thenReturn(1);

        int result = roomService.setRoomAvailable("101");

        assertEquals(1, result);
        verify(roomRepository).updateStatusByRoomNumber("101", RoomStatus.AVAILABLE);
    }

    @Test
    void getRoomReferenceByRoom_shouldReturnRoom() {
        Room room = new Room();
        room.setRoomNumber("101");
        when(roomRepository.findByRoomNumber("101")).thenReturn(Optional.of(room));

        Room result = roomService.getRoomReferenceByRoom("101");

        assertEquals(room, result);
        verify(roomRepository).findByRoomNumber("101");
    }

    @Test
    void getRoomReferenceByRoom_shouldThrowResourceNotFoundException() {
        when(roomRepository.findByRoomNumber("101")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> roomService.getRoomReferenceByRoom("101"));
        assertEquals("Room not found.", exception.getMessage());
    }
}
