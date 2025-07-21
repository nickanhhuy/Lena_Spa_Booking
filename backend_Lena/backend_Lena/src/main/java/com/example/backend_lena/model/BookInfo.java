package com.example.backend_lena.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "bookings")
@Entity
public class BookInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String service;
    private LocalDateTime bookingDate;

    public BookInfo(String name, String email, String phone, String service, LocalDateTime bookingDate) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.service = service;
        this.bookingDate = bookingDate;
    }

    public BookInfo() {

    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
