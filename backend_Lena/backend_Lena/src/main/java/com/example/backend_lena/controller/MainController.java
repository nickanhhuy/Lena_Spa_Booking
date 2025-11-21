package com.example.backend_lena.controller;

import com.example.backend_lena.model.BookInfo;
import com.example.backend_lena.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class MainController {

    @Autowired
    private BookingService bookingService;

    /** Get all bookings */
    @GetMapping("/bookings")
    public List<BookInfo> getBookingLists(Authentication authentication) {
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            // Admin sees all bookings
            return bookingService.getBookings();
        } else {
            // Regular user sees only their own bookings
            return bookingService.getByCreatedBy(username);
        }
    }

    /** Add a new booking */
    @PostMapping("/bookings/addbooking")
    public BookInfo addBooking(@Valid @RequestBody BookInfo booking, Authentication authentication) {
        String username = authentication.getName();
        booking.setCreatedBy(username);
        return bookingService.addOrUpdateBookInfo(booking);
    }

    /** Update an existing booking */
    @PutMapping("/bookings/{id}/update")
    public BookInfo updateBooking(@Valid @RequestBody BookInfo updatedBooking,
                                  @PathVariable Long id,
                                  Authentication authentication) {
        String username = authentication.getName();

        return bookingService.getBookingById(id).map(existingBooking -> {
            // Only allow owner or admin to update
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if (!isAdmin && !existingBooking.getCreatedBy().equals(username)) {
                throw new RuntimeException("Unauthorized to update this booking");
            }

            existingBooking.setName(updatedBooking.getName());
            existingBooking.setEmail(updatedBooking.getEmail());
            existingBooking.setPhone(updatedBooking.getPhone());
            existingBooking.setService(updatedBooking.getService());
            existingBooking.setBookingDate(updatedBooking.getBookingDate());

            return bookingService.addOrUpdateBookInfo(existingBooking);
        }).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    /** Delete a booking */
    @DeleteMapping("/bookings/{id}")
    public void deleteBooking(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        bookingService.getBookingById(id).ifPresent(booking -> {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if (!isAdmin && !booking.getCreatedBy().equals(username)) {
                throw new RuntimeException("Unauthorized to delete this booking");
            }

            bookingService.deleteById(id);
        });
    }
}

