package com.example.backend_lena.controller;


import com.example.backend_lena.model.BookInfo;
import com.example.backend_lena.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class MainController {

    @Autowired
    BookingService bookingService;

    @GetMapping("/bookings") // get lists of bookings
    public List<BookInfo> getBookingLists() {
        return bookingService.getBookings();
    }

    @PostMapping("/bookings/addbooking") // add a new booking
    public BookInfo addBooking(@RequestBody BookInfo booking) {
       return bookingService.addOrUpdateBookInfo(booking);
    }

    @PutMapping("/bookings/{id}/update") // edit the information of the booking
    public BookInfo updateBooking(@RequestBody BookInfo updated_booking, @PathVariable Long id) {
        return bookingService.getBookingById(id).map(existing_booking -> {
            existing_booking.setName(updated_booking.getName());
            existing_booking.setEmail(updated_booking.getEmail());
            existing_booking.setPhone(updated_booking.getPhone());
            existing_booking.setService(updated_booking.getService());
            existing_booking.setBookingDate(updated_booking.getBookingDate());
            return bookingService.addOrUpdateBookInfo(existing_booking);
        }).orElseThrow();
    }
    @DeleteMapping("/bookings/{id}") // delete or cancel the booking
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteById(id);
    }

    @GetMapping("/user")
    public List<BookInfo> getBookingsByEmail(@RequestParam String email) {
        return bookingService.getByEmail(email);
    }
}
