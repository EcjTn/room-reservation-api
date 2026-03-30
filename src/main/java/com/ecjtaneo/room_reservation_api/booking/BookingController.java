package com.ecjtaneo.room_reservation_api.booking;

import com.ecjtaneo.room_reservation_api.booking.dto.BookingCreationDto;
import com.ecjtaneo.room_reservation_api.booking.dto.BookingPublicResponseDto;
import com.ecjtaneo.room_reservation_api.common.dto.MessageResponseDto;
import com.ecjtaneo.room_reservation_api.infrastructure.security.UserDetailsImpl;
import jakarta.validation.Valid;
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
    @PreAuthorize("hasAuthority('ADMIN') or @bookingService.canCancelBooking(#id, principal.userId)")  //principal is automatically available in spel expressions inside @PreAuthorize
    public MessageResponseDto cancelBooking(@PathVariable("id") Long id) {
        return bookingService.cancelBooking(id);
    }

    @PatchMapping("/confirm/{id}")
    public MessageResponseDto confirmBooking(@PathVariable("id") Long id) {
        return bookingService.confirmBooking(id);
    }

    @PatchMapping("/complete/{id}")
    public MessageResponseDto completeBooking(@PathVariable("id") Long id) {
        return bookingService.completeBooking(id);
    }

}
