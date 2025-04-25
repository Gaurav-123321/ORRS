package com.rms.bookingservice.service;

import org.springframework.stereotype.Service;
import com.rms.bookingservice.dto.BookingDto;
import com.rms.bookingservice.entity.Booking;
import com.rms.bookingservice.feign.SearchServiceClient;
import com.rms.bookingservice.feign.UserServiceClient;
import com.rms.bookingservice.repository.BookingRepository;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final SearchServiceClient searchServiceClient;
    private final UserServiceClient userServiceClient;

    @Override
    public Booking add(BookingDto bookingDto) {
        searchServiceClient.getTrainById(bookingDto.getTrainId());
        userServiceClient.getUserById(bookingDto.getUserId());
        Booking booking = new Booking(
            null,
            bookingDto.getUserId(),
            bookingDto.getTrainId(),
            bookingDto.getSeatCount(),
            bookingDto.getPaymentType(),
            bookingDto.getStatus(),
            bookingDto.getTrainNumber(),
            bookingDto.getSource(),
            bookingDto.getDestination(),
            false
        );
        return bookingRepository.save(booking);
    }

    @Override
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingByUserId(Long userId) {
        return bookingRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Booking not found for user"));
    }

    @Override
    public void updateBookingStatus(Long bookingId, String status) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            booking.setStatus(status);
            bookingRepository.save(booking);
        }
    }

    @Override
    public String cancelTicket(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new RuntimeException("Booking not found or unauthorized"));

        if (booking.isCancelled()) {
            return "Booking is already cancelled";
        }

        booking.setCancelled(true);
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        searchServiceClient.restoreSeats(booking.getTrainId(), booking.getSeatCount());

        return "Booking cancelled and seats restored successfully";
    }

}
