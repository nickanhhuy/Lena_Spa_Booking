package com.example.backend_lena.service;

import com.example.backend_lena.model.BookInfo;
import com.example.backend_lena.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    public BookInfo addOrUpdateBookInfo(BookInfo bookInfo) {
        return bookingRepository.save(bookInfo);
    }

    public List<BookInfo> getBookings(BookInfo bookInfo) {
        return bookingRepository.findAll(); //User can see all their bookings or Admin can see all the booking of all users
    }

    public List<BookInfo> getByEmail(String email) {
        return bookingRepository.findByEmail(email);
    }

    public Optional<BookInfo> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

}
