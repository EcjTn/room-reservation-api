package com.ecjtaneo.room_reservation_api.booking;

import com.ecjtaneo.room_reservation_api.booking.dto.BookingCreationDto;
import com.ecjtaneo.room_reservation_api.booking.model.Booking;
import com.ecjtaneo.room_reservation_api.booking.model.BookingStatus;
import com.ecjtaneo.room_reservation_api.booking.model.PaymentStatus;
import com.ecjtaneo.room_reservation_api.common.dto.MessageResponseDto;
import com.ecjtaneo.room_reservation_api.common.exception.ResourceNotFoundException;
import com.ecjtaneo.room_reservation_api.common.exception.ValidationException;
import com.ecjtaneo.room_reservation_api.room.RoomService;
import com.ecjtaneo.room_reservation_api.room.model.Room;
import com.ecjtaneo.room_reservation_api.user.UserService;
import com.ecjtaneo.room_reservation_api.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomService roomService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void createBooking_shouldCreateBookingSuccessfully() {
        BookingCreationDto dto = new BookingCreationDto("101", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        Long userId = 1L;

        Room room = new Room();
        room.setRoomNumber("101");
        room.setPricePerNight(BigDecimal.valueOf(100));

        User user = new User();
        user.setId(userId);

        when(roomService.setRoomBookedIfAvailable("101")).thenReturn(1);
        when(roomService.getRoomReferenceByRoom("101")).thenReturn(room);
        when(userService.getUserReference(userId)).thenReturn(user);
        when(bookingRepository.save(any(Booking.class))).thenReturn(new Booking());

        MessageResponseDto result = bookingService.createBooking(dto, userId);

        assertEquals("Booking successfully created.", result.message());
        verify(roomService).setRoomBookedIfAvailable("101");
        verify(roomService).getRoomReferenceByRoom("101");
        verify(userService).getUserReference(userId);
        verify(bookingRepository).save(any(Booking.class));
        verify(roomService).setRoomBooked("101");
    }

    @Test
    void createBooking_shouldThrowValidationExceptionWhenDatesInvalid() {
        BookingCreationDto dto = new BookingCreationDto("101", LocalDate.now().plusDays(3), LocalDate.now().plusDays(1));
        Long userId = 1L;

        ValidationException exception = assertThrows(ValidationException.class, () -> bookingService.createBooking(dto, userId));
        assertEquals("Invalid dates", exception.getMessage());
    }

    @Test
    void createBooking_shouldThrowValidationExceptionWhenRoomNotAvailable() {
        BookingCreationDto dto = new BookingCreationDto("101", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        Long userId = 1L;

        when(roomService.setRoomBookedIfAvailable("101")).thenReturn(0);

        ValidationException exception = assertThrows(ValidationException.class, () -> bookingService.createBooking(dto, userId));
        assertEquals("Room not available", exception.getMessage());
    }

    @Test
    void deleteBooking_shouldDeleteSuccessfully() {
        Long id = 1L;
        when(bookingRepository.deleteBookingById(id)).thenReturn(1);

        MessageResponseDto result = bookingService.deleteBooking(id);

        assertEquals("Booking successfully deleted.", result.message());
        verify(bookingRepository).deleteBookingById(id);
    }

    @Test
    void deleteBooking_shouldThrowResourceNotFoundException() {
        Long id = 1L;
        when(bookingRepository.deleteBookingById(id)).thenReturn(0);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> bookingService.deleteBooking(id));
        assertEquals("Booking not found", exception.getMessage());
    }

    @Test
    void cancelBooking_shouldCancelSuccessfully() {
        Long id = 1L;
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.PENDING);
        Room room = new Room();
        room.setRoomNumber("101");
        booking.setRoom(room);

        when(bookingRepository.findByIdAndStatus(id, BookingStatus.PENDING)).thenReturn(Optional.of(booking));

        MessageResponseDto result = bookingService.cancelBooking(id);

        assertEquals("Booking successfully cancelled.", result.message());
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        verify(roomService).setRoomAvailable("101");
    }

    @Test
    void cancelBooking_shouldThrowResourceNotFoundException() {
        Long id = 1L;
        when(bookingRepository.findByIdAndStatus(id, BookingStatus.PENDING)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> bookingService.cancelBooking(id));
        assertEquals("Booking not found", exception.getMessage());
    }

    @Test
    void confirmBooking_shouldConfirmSuccessfully() {
        Long id = 1L;
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.PENDING);
        Room room = new Room();
        room.setRoomNumber("101");
        booking.setRoom(room);

        when(bookingRepository.findByIdAndStatus(id, BookingStatus.PENDING)).thenReturn(Optional.of(booking));

        MessageResponseDto result = bookingService.confirmBooking(id);

        assertEquals("Booking successfully confirmed.", result.message());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals(PaymentStatus.PAID, booking.getPaymentStatus());
        verify(roomService).setRoomOccupied("101");
    }

    @Test
    void completeBooking_shouldCompleteSuccessfully() {
        Long id = 1L;
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.CONFIRMED);
        Room room = new Room();
        room.setRoomNumber("101");
        booking.setRoom(room);

        when(bookingRepository.findByIdAndStatus(id, BookingStatus.CONFIRMED)).thenReturn(Optional.of(booking));

        MessageResponseDto result = bookingService.completeBooking(id);

        assertEquals("Booking successfully completed.", result.message());
        assertEquals(BookingStatus.COMPLETED, booking.getStatus());
        verify(roomService).setRoomAvailable("101");
    }

    @Test
    void canCancelBooking_shouldReturnTrue() {
        Long id = 1L;
        Long userId = 1L;
        when(bookingRepository.existsBookingByIdAndUserId(id, userId)).thenReturn(true);

        boolean result = bookingService.canCancelBooking(id, userId);

        assertTrue(result);
        verify(bookingRepository).existsBookingByIdAndUserId(id, userId);
    }

    @Test
    void canCancelBooking_shouldReturnFalse() {
        Long id = 1L;
        Long userId = 1L;
        when(bookingRepository.existsBookingByIdAndUserId(id, userId)).thenReturn(false);

        boolean result = bookingService.canCancelBooking(id, userId);

        assertFalse(result);
        verify(bookingRepository).existsBookingByIdAndUserId(id, userId);
    }
}
