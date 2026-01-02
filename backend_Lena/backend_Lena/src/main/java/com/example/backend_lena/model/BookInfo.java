package com.example.backend_lena.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Table(name = "bookings")
@Entity
public class BookInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name required")
    private String name;

    // Email is optional - will be populated from user account
    @Email(message = "Invalid email format")
    private String email;

    //Phone number including 10-11 digits
    @NotBlank(message = "Phone required")
    @Pattern(regexp = "^[0-9]{10,11}", message = "Phone number must include 10 or 11 digit")
    private String phone;

    @NotBlank(message = "Service required")
    private String service;

    private LocalDateTime bookingDate;

    @Column(name = "created_by")
    private String createdBy;

    public BookInfo(Long id, String name, String email, String phone, String service, LocalDateTime bookingDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.service = service;
        this.bookingDate = bookingDate;
    }

    public BookInfo() {

    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
