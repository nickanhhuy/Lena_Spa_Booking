package com.example.backend_lena.service;

import com.example.backend_lena.emails.EmailServiceImpl;
import com.example.backend_lena.model.BookInfo;
import com.example.backend_lena.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmailServiceImpl emailService;

    public BookInfo addOrUpdateBookInfo(BookInfo booking) {
        BookInfo saved_bookInfo = bookingRepository.save(booking);
        emailService.sendBookingConfirmation(saved_bookInfo.getCreatedBy(), saved_bookInfo);
        return saved_bookInfo;
    }

    public List<BookInfo> getBookings() {
        return bookingRepository.findAll(); //User can see all their bookings or Admin can see all the booking of all users
    }

    public Optional<BookInfo> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

    public List<BookInfo> getByCreatedBy(String createdBy) {
        return bookingRepository.findByCreatedBy(createdBy);
    }
}
