package com.rms.bookingservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rms.bookingservice.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByUserId(Long userId);
    List<Booking> findAll();
    Optional<Booking> findByIdAndUserId(Long id, Long userId);
}
