package com.ecjtaneo.hotel_management_system.booking;

import com.ecjtaneo.hotel_management_system.booking.dto.BookingCreationDto;
import com.ecjtaneo.hotel_management_system.booking.dto.BookingPublicResponseDto;
import com.ecjtaneo.hotel_management_system.booking.model.Booking;
import com.ecjtaneo.hotel_management_system.booking.model.BookingStatus;
import com.ecjtaneo.hotel_management_system.booking.model.PaymentStatus;
import com.ecjtaneo.hotel_management_system.common.dto.MessageResponseDto;
import com.ecjtaneo.hotel_management_system.common.exception.ResourceNotFoundException;
import com.ecjtaneo.hotel_management_system.common.exception.ValidationException;
import com.ecjtaneo.hotel_management_system.room.RoomService;
import com.ecjtaneo.hotel_management_system.room.model.Room;
import com.ecjtaneo.hotel_management_system.user.UserService;
import com.ecjtaneo.hotel_management_system.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final UserService userService;

    public BookingService(BookingRepository bookingRepository, RoomService roomService, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
        this.userService = userService;
    }

    public MessageResponseDto createBooking(BookingCreationDto bookingCreationDto, Long userId) {

        if(bookingCreationDto.startDate().isAfter(bookingCreationDto.endDate())) throw new ValidationException("End date must be after start date p");

        Room room = roomService.findAvailableRoom(bookingCreationDto.roomNumber());
        User userRef = userService.getUserReference(userId);

        BigDecimal totalAmount = room.getPricePerNight()
                .multiply(new BigDecimal(
                        bookingCreationDto.endDate().toEpochDay() - bookingCreationDto.startDate().toEpochDay()
                ));

        Booking booking = new Booking();

        booking.setRoom(room);
        booking.setUser(userRef);

        booking.setStartDate(bookingCreationDto.startDate());
        booking.setEndDate(bookingCreationDto.endDate());
        booking.setTotalAmount(totalAmount);

        booking.setStatus(BookingStatus.PENDING);

        bookingRepository.save(booking);
        roomService.setRoomBooked(room.getRoomNumber());

        return new MessageResponseDto("Booking successfully created.");
    }

    @Transactional
    public MessageResponseDto deleteBooking(Long id) {
        if(bookingRepository.deleteBookingById(id) <= 0) throw new ResourceNotFoundException("Booking not found");
        return new MessageResponseDto("Booking successfully deleted.");
    }

    @Transactional
    public MessageResponseDto cancelBooking(Long id) {
        Booking booking = bookingRepository.findByIdAndStatus(id, BookingStatus.PENDING)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setStatus(BookingStatus.CANCELLED);

        roomService.setRoomAvailable(booking.getRoom().getRoomNumber());

        return new MessageResponseDto("Booking successfully cancelled.");
    }

    public boolean canCancelBooking(Long id, Long userId) {
        return bookingRepository.existsBookingByIdAndUserId(id, userId);
    }

    public List<BookingPublicResponseDto> getBookingsByUserId(Long userId) {
        return bookingRepository.findTop10ByUserIdOrderByIdDesc(userId);
    }

    public List<BookingPublicResponseDto> getBookingsBeforeByUserId(Long lastSeenId, Long userId) {
        return bookingRepository.findTop10ByIdLessThanAndUserIdOrderByIdDesc(lastSeenId, userId);
    }

    public long getBookingsCount() {
        return bookingRepository.count();
    }

    public long getActiveBookingsCount() {
        return bookingRepository.countByStatus(BookingStatus.CONFIRMED);
    }

    public long getCompletedBookingsCount() {
        return bookingRepository.countByStatus(BookingStatus.COMPLETED);
    }

    public BigDecimal calculateTotalRevenue() {
        return bookingRepository.sumTotalAmountByStatus(PaymentStatus.PAID);
    }

    public BigDecimal calculateRevenueBetweenDates(LocalDateTime start, LocalDateTime end) {
        return bookingRepository.sumByStatusAndCreatedAtBetween(PaymentStatus.PAID, start, end);
    }

    //Admins only operations
    @Transactional
    public MessageResponseDto confirmBooking(Long id) {
        Booking booking = bookingRepository.findByIdAndStatus(id, BookingStatus.PENDING)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        roomService.setRoomOccupied(booking.getRoom().getRoomNumber());
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentStatus(PaymentStatus.PAID);

        return new MessageResponseDto("Booking successfully confirmed.");
    }

    @Transactional
    public MessageResponseDto completeBooking(Long id) {
        Booking booking = bookingRepository.findByIdAndStatus(id, BookingStatus.CONFIRMED)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        roomService.setRoomAvailable(booking.getRoom().getRoomNumber());
        booking.setStatus(BookingStatus.COMPLETED);

        return new MessageResponseDto("Booking successfully completed.");
    }

}
