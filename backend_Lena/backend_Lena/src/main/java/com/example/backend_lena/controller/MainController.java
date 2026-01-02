package com.example.backend_lena.controller;

import com.example.backend_lena.model.BookInfo;
import com.example.backend_lena.model.User;
import com.example.backend_lena.repository.UserRepository;
import com.example.backend_lena.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    BookingService bookingService;
    
    @Autowired
    UserRepository userRepository;

    @GetMapping("/bookings") //getting booking lists

    public List<BookInfo> getBookingLists(Authentication authentication) {
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return bookingService.getBookings();  // all bookings

        } else {
            return bookingService.getByCreatedBy(username); // only this user's bookings

        }
    }

    @GetMapping("/bookings/available-slots")
    public List<String> getAvailableSlots(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return bookingService.getAvailableTimeSlots(localDate);
    }

    @PostMapping("/bookings/addbooking") // book a new appointment
    public BookInfo addBooking(@Valid @RequestBody BookInfo booking, Authentication authentication) {
        String username = authentication.getName();
        System.out.println("Booking created by: " + username);
        
        // Set the username who created the booking
        booking.setCreatedBy(username);
        
        // Get user's email from database and set it to the booking
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        booking.setEmail(user.getEmail());
        
        return bookingService.addOrUpdateBookInfo(booking);
    }

    @PutMapping("/bookings/{id}/update") // edit the information of the booking
    public BookInfo updateBooking(@Valid @RequestBody BookInfo updated_booking, @PathVariable Long id) {
        return bookingService.getBookingById(id).map(existing_booking -> {
            existing_booking.setName(updated_booking.getName());
            existing_booking.setEmail(updated_booking.getEmail());
            existing_booking.setPhone(updated_booking.getPhone());
            existing_booking.setService(updated_booking.getService());
            existing_booking.setBookingDate(updated_booking.getBookingDate());
            return bookingService.addOrUpdateBookInfo(existing_booking);
        }).orElseThrow();
    }
    //delete bookings
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
