package com.ecjtaneo.hotel_management_system.booking;

import com.ecjtaneo.hotel_management_system.booking.dto.BookingCreationDto;
import com.ecjtaneo.hotel_management_system.common.dto.MessageResponseDto;
import com.ecjtaneo.hotel_management_system.infrastructure.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public MessageResponseDto createBooking(
            @RequestBody @Valid BookingCreationDto bookingCreationDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUserId();
        return bookingService.createBooking(bookingCreationDto, userId);
    }

    @DeleteMapping("/{id}")
    public MessageResponseDto deleteBooking(@PathVariable("id") Long id) {
        return bookingService.deleteBooking(id);
    }

    @PatchMapping("/cancel/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or bookingService.canCancelBooking(#id, principal.userId)")  //principal is automatically available in spel expressions inside @PreAuthorize
    public MessageResponseDto cancelBooking(@PathVariable("id") Long id) {
        return bookingService.cancelBooking(id);
    }

}
