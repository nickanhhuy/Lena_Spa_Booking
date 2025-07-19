package com.example.backend_lena.controller;


import com.example.backend_lena.model.BookInfo;
import com.example.backend_lena.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin("http://localhost:4200")
public class MainController {

    @Autowired
    BookingService bookingService;

    @GetMapping("/bookings")
    public List<BookInfo> getBookingLists() {
        return bookingService.getBookings();
    }
    @PostMapping("/bookings/addbooking")
    public void addBooking(@RequestBody BookInfo booking) {
        bookingService.addOrUpdateBookInfo(booking);
    }



}
