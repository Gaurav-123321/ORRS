package com.rms.bookingservice.service;

import com.rms.bookingservice.dto.BookingDto;
import com.rms.bookingservice.entity.Booking;
import java.util.List;

public interface BookingService {
    List<Booking> getAllBookings();

    Booking getBookingById(Long id);

    // Booking createBooking(Booking booking);
    Booking cancelBooking(Long id);

    Booking add(BookingDto bookingDto);

    String makePayment(Long bookingId);

    /**
     * Partially cancel tickets from a booking.
     * 
     * @param bookingId     The booking ID
     * @param seatsToCancel Number of seats to cancel
     * @return Updated Booking
     */
    Booking partialCancelBooking(Long bookingId, int seatsToCancel);

}
