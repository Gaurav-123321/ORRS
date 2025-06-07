package com.rms.bookingservice.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.rms.bookingservice.entity.Booking;
import com.rms.bookingservice.entity.BookingStatus;
import com.rms.bookingservice.repository.BookingRepository;
import com.rms.bookingservice.exception.BookingNotFoundException;
import com.rms.bookingservice.feign.TrainServiceClient;
import com.rms.bookingservice.dto.TrainDto;

import jakarta.transaction.Transactional;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final RazorpayClient razorpayClient;
    private final BookingRepository bookingRepository;
    private final TrainServiceClient trainServiceClient;

    public PaymentService(RazorpayClient razorpayClient, BookingRepository bookingRepository,
            TrainServiceClient trainServiceClient) {
        this.razorpayClient = razorpayClient;
        this.bookingRepository = bookingRepository;
        this.trainServiceClient = trainServiceClient;
    }

    public String generatePaymentLink(Booking booking) throws RazorpayException {
        logger.info("Generating payment link for booking {}", booking.getId());

        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", booking.getTotalFare() * 100); // Amount in paise
        paymentLinkRequest.put("currency", "INR");
        paymentLinkRequest.put("description", "Payment for Booking #" + booking.getId());
        paymentLinkRequest.put("callback_url", "http://localhost:8080/payment/status?bookingId=" + booking.getId());
        paymentLinkRequest.put("callback_method", "get");

        PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);
        return paymentLink.get("short_url").toString();
    }

    @Transactional
    public void changeStatus(String paymentId, Long bookingId, String status) throws BookingNotFoundException {
        logger.info("Changing status for booking {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        if ("paid".equalsIgnoreCase(status)) {
            logger.info("Payment successful for booking {}", bookingId);
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.setPaymentType(paymentId);
            // Update available seats after payment confirmation
            TrainDto train = trainServiceClient.getTrainById(booking.getTrainId());
            int newAvailableSeats = train.getAvailableSeats() - booking.getNoOfSeats();
            // Use BookingServiceImpl's centralized method if possible, else call directly
            trainServiceClient.updateAvailableSeats(booking.getTrainId(), newAvailableSeats);
            logger.info("Updated available seats for Train ID {} to {} after payment", booking.getTrainId(),
                    newAvailableSeats);
        } else {
            logger.info("Payment failed for booking {}", bookingId);
            booking.setStatus(BookingStatus.CANCELLED);
        }
        bookingRepository.save(booking);
        logger.info("Booking status updated for booking {}", bookingId);
    }
}
