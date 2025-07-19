package com.example.backend_lena.repository;

import com.example.backend_lena.model.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository  extends JpaRepository<BookInfo, Long> {
    List<BookInfo> findByEmail(String email);
    List<BookInfo> findByDateTime(LocalDateTime startDate, LocalDateTime endDate);
}
