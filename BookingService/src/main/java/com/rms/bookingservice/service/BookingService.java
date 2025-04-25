package com.rms.bookingservice.service;

import java.util.List;

import com.rms.bookingservice.dto.BookingDto;
import com.rms.bookingservice.entity.Booking;


public interface BookingService {
    Booking add(BookingDto bookingDto);
    void delete(Long id);
    void updateBookingStatus(Long bookingId, String status);
    List<Booking> getAllBookings();
    Booking getBookingByUserId(Long userId);
    String cancelTicket(Long bookingId, Long userId);
}