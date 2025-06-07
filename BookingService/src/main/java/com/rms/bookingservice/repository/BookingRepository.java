package com.rms.bookingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rms.bookingservice.entity.Booking;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByUserId(Long userId);
    
    Optional<Booking> findById(Long bookingId);

}
