package com.rms.bookingservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.razorpay.RazorpayException;
import com.rms.bookingservice.dto.BookingDto;
import com.rms.bookingservice.dto.TrainDto;
import com.rms.bookingservice.dto.UserDto;
import com.rms.bookingservice.entity.Booking;
import com.rms.bookingservice.entity.BookingStatus;
import com.rms.bookingservice.exception.BookingNotFoundException;
import com.rms.bookingservice.exception.UserNotFoundException;
import com.rms.bookingservice.feign.TrainServiceClient;
import com.rms.bookingservice.feign.UserServiceClient;
import com.rms.bookingservice.repository.BookingRepository;

import feign.FeignException;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private TrainServiceClient trainServiceClient;

    @Autowired
    private PaymentService paymentService;

    @Override
    public Booking add(BookingDto bookingDto) {
        Long trainId = bookingDto.getTrainId();
        Long userId = bookingDto.getUserId();
        String paymentType = bookingDto.getPaymentType();
        int noOfSeats = bookingDto.getNoOfSeats();

        logger.info("Starting booking process for User ID {} and Train ID {} for {} seats", userId, trainId, noOfSeats);

        UserDto user;
        try {
            user = userServiceClient.getUserById(userId);
            if (user == null) {
                logger.error("User not found with ID: {}", userId);
                throw new UserNotFoundException("User not found with ID: " + userId);
            }
        } catch (FeignException.NotFound e) {
            logger.error("User service returned Not Found for ID: {}", userId);
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        TrainDto train;
        try {
            train = trainServiceClient.getTrainById(trainId);
            if (train == null || train.getAvailableSeats() < noOfSeats) {
                logger.error("Not enough available seats for Train ID: {}. Requested: {}, Available: {}", trainId,
                        noOfSeats, train != null ? train.getAvailableSeats() : 0);
                throw new RuntimeException("Not enough seats available");
            }
        } catch (FeignException.NotFound e) {
            logger.error("Train service returned Not Found for ID: {}", trainId);
            throw new RuntimeException("Train not available!");
        }

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setTrainId(trainId);
        booking.setPaymentType(paymentType);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setNoOfSeats(noOfSeats);
        double fare = train.getPrice() * noOfSeats;
        booking.setTotalFare(fare);
        booking.setJourneyDate(bookingDto.getJourneyDate());
        logger.warn("DEBUG: About to save booking with noOfSeats={} and totalFare={} and journeyDate={}", noOfSeats,
                fare, bookingDto.getJourneyDate());

        Booking savedBooking = bookingRepo.save(booking);
        logger.info("Booking confirmed successfully: {}", savedBooking);

        // Update available seats in TrainService immediately after booking
        try {
            int newAvailableSeats = train.getAvailableSeats() - noOfSeats;
            logger.warn("DEBUG: Calling updateAvailableSeats with trainId={} and availableSeats={}", trainId,
                    newAvailableSeats);
            trainServiceClient.updateAvailableSeats(trainId, newAvailableSeats);
            logger.info("Updated available seats for Train ID {} after booking", trainId);
        } catch (Exception e) {
            logger.error("Failed to update seat count after booking for train ID {}. Exception: {}", trainId,
                    e.getMessage(), e);
            throw new RuntimeException("Failed to update seat count after booking.");
        }

        return savedBooking;
    }

    @Override
    @Transactional
    public Booking cancelBooking(Long id) {
        logger.info("Attempting to cancel booking ID {}", id);

        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Booking not found with ID: {}", id);
                    return new BookingNotFoundException("Booking not found with ID: " + id);
                });

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            logger.warn("Booking ID {} is already cancelled", id);
            throw new IllegalStateException("Booking is already cancelled.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepo.save(booking);
        logger.info("Booking ID {} cancelled successfully", id);

        try {
            TrainDto train = trainServiceClient.getTrainById(booking.getTrainId());
            trainServiceClient.updateAvailableSeats(booking.getTrainId(),
                    train.getAvailableSeats() + booking.getNoOfSeats());
            logger.info("Updated available seats for Train ID {} after cancellation", booking.getTrainId());
        } catch (Exception e) {
            logger.error("Failed to update seat count after cancellation for booking ID {}", id);
            throw new RuntimeException("Failed to update seat count after cancellation.");
        }

        return updatedBooking;
    }

    @Override
    public List<Booking> getAllBookings() {
        logger.info("Fetching all bookings");
        List<Booking> bookings = bookingRepo.findAll();
        logger.info("Total bookings found: {}", bookings.size());
        return bookings;
    }

    @Override
    public Booking getBookingById(Long id) {
        logger.info("Fetching booking with ID: {}", id);
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Booking not found for ID: {}", id);
                    return new BookingNotFoundException("Booking not found for ID: " + id);
                });
        logger.info("Booking found: {}", booking);
        return booking;
    }

    // @Override
    // public void changeStatus(String paymentId, Long bookingId, String status) {
    // Booking booking = bookingRepo.findById(bookingId)
    // .orElseThrow(() -> new BookingNotFoundException("Booking not found"));
    // if ("paid".equalsIgnoreCase(status)) {
    // booking.setStatus(BookingStatus.CONFIRMED);
    // booking.setPaymentId(paymentId);
    // } else {
    // booking.setStatus(BookingStatus.CANCELLED);
    // }
    // bookingRepo.save(booking);
    // }

    @Transactional
    public String makePayment(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + bookingId));

        // if (!booking.getStatus().equals("CANCELLED") ) {
        // return "Only VERIFIED or FAILED orders can be paid";
        // }

        String paymentLink = null;
        try {
            paymentLink = paymentService.generatePaymentLink(booking);
        } catch (RazorpayException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "Complete your payment here: " + paymentLink;
    }

    /**
     * Centralized method to update available seats for a train
     */
    private void updateAvailableSeats(Long trainId, int newAvailableSeats) {
        try {
            trainServiceClient.updateAvailableSeats(trainId, newAvailableSeats);
            logger.info("Updated available seats for Train ID {} to {}", trainId, newAvailableSeats);
        } catch (Exception e) {
            logger.error("Failed to update seat count for train ID {}. Exception: {}", trainId, e.getMessage(), e);
            throw new RuntimeException("Failed to update seat count.");
        }
    }

    @Override
    public Booking partialCancelBooking(Long bookingId, int seatsToCancel) {
        throw new UnsupportedOperationException("Partial cancellation not implemented yet");
    }

}