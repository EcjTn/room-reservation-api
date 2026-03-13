package com.ecjtaneo.hotel_management_system.booking;

import com.ecjtaneo.hotel_management_system.booking.dto.BookingPublicResponseDto;
import com.ecjtaneo.hotel_management_system.infrastructure.security.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserBookingController {
    private final BookingService bookingService;

    public UserBookingController(BookingService bookingMapper) {
        this.bookingService = bookingMapper;
    }

    @GetMapping("/me/bookings")
    public List<BookingPublicResponseDto> showBookings(
            @RequestParam(name = "cursor", required = false) Long cursor,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long userId = userDetails.getUserId();
        if(cursor == null) return bookingService.getBookingsByUserId(userId);
        return bookingService.getBookingsBeforeByUserId(cursor, userId);
    }

    @GetMapping("/{id}/bookings")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<BookingPublicResponseDto> showBookingsByUserId(
            @RequestParam(name = "cursor", required = false) Long cursor,
            @PathVariable("id") Long userId
    ) {
        if(cursor == null) return bookingService.getBookingsByUserId(userId);
        return bookingService.getBookingsBeforeByUserId(cursor, userId);
    }

}
