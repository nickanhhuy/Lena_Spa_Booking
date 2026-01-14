package com.example.backend_lena.service;

import com.example.backend_lena.emails.EmailServiceImpl;
import com.example.backend_lena.model.BookInfo;
import com.example.backend_lena.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ResendEmailService resendEmailService;

    public BookInfo addOrUpdateBookInfo(BookInfo booking) {
        // Set status to CONFIRMED for new bookings
        if (booking.getStatus() == null || booking.getStatus().isEmpty()) {
            booking.setStatus("CONFIRMED");
        }
        
        BookInfo saved_bookInfo = bookingRepository.save(booking);
        
        // Send confirmation email to customer
        try {
            resendEmailService.sendBookingConfirmation(saved_bookInfo.getCreatedBy(), saved_bookInfo);
        } catch (Exception e) {
            System.err.println("Failed to send booking confirmation: " + e.getMessage());
        }
        
        // Send notification email to admin
        try {
            resendEmailService.sendAdminBookingNotification(saved_bookInfo);
        } catch (Exception e) {
            System.err.println("Failed to send admin booking notification: " + e.getMessage());
        }
        
        return saved_bookInfo;
    }

    public BookInfo cancelBooking(Long bookingId, String cancellationReason) {
        Optional<BookInfo> bookingOpt = bookingRepository.findById(bookingId);
        
        if (bookingOpt.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }
        
        BookInfo booking = bookingOpt.get();
        
        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("Booking is already cancelled");
        }
        
        booking.setStatus("CANCELLED");
        booking.setCancellationReason(cancellationReason);
        booking.setCancelledAt(LocalDateTime.now());
        
        BookInfo cancelledBooking = bookingRepository.save(booking);
        
        // Send cancellation email to customer
        try {
            resendEmailService.sendBookingCancellationEmail(
                cancelledBooking.getCreatedBy(), 
                cancelledBooking
            );
        } catch (Exception e) {
            System.err.println("Failed to send cancellation email: " + e.getMessage());
        }
        
        return cancelledBooking;
    }

    public BookInfo rescheduleBooking(Long bookingId, LocalDateTime newBookingDate) {
        Optional<BookInfo> bookingOpt = bookingRepository.findById(bookingId);
        
        if (bookingOpt.isEmpty()) {
            throw new RuntimeException("Booking not found");
        }
        
        BookInfo booking = bookingOpt.get();
        
        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("Cannot reschedule a cancelled booking");
        }
        
        LocalDateTime oldDate = booking.getBookingDate();
        booking.setBookingDate(newBookingDate);
        booking.setStatus("RESCHEDULED");
        
        BookInfo rescheduledBooking = bookingRepository.save(booking);
        
        // Send rescheduling email to customer
        try {
            resendEmailService.sendBookingRescheduleEmail(
                rescheduledBooking.getCreatedBy(), 
                rescheduledBooking,
                oldDate
            );
        } catch (Exception e) {
            System.err.println("Failed to send reschedule email: " + e.getMessage());
        }
        
        return rescheduledBooking;
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

    public List<String> getAvailableTimeSlots(LocalDate date) {
        // Define business hours: 9 AM to 6 PM, 1-hour slots
        List<String> allSlots = new ArrayList<>();
        for (int hour = 9; hour <= 17; hour++) {
            allSlots.add(String.format("%02d:00", hour));
        }

        // Get all bookings for the specified date
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        
        List<BookInfo> bookingsOnDate = bookingRepository.findAll().stream()
                .filter(booking -> {
                    LocalDateTime bookingDateTime = booking.getBookingDate();
                    return bookingDateTime != null && 
                           !bookingDateTime.isBefore(startOfDay) && 
                           !bookingDateTime.isAfter(endOfDay);
                })
                .collect(Collectors.toList());

        // Remove booked time slots
        List<String> bookedSlots = bookingsOnDate.stream()
                .map(booking -> String.format("%02d:00", booking.getBookingDate().getHour()))
                .collect(Collectors.toList());

        allSlots.removeAll(bookedSlots);
        return allSlots;
    }
}
