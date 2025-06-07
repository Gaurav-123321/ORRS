package com.rms.bookingservice.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.rms.bookingservice.exception.BookingNotFoundException;
import com.rms.bookingservice.service.PaymentService;
import com.rms.bookingservice.entity.Booking;
import com.rms.bookingservice.repository.BookingRepository;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final RazorpayClient razorpayClient;
    private final PaymentService service;
    private final BookingRepository bookingRepository;

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    public PaymentController(RazorpayClient razorpayClient, PaymentService service, BookingRepository bookingRepository) {
        this.razorpayClient = razorpayClient;
        this.service = service;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/status")
    public ResponseEntity<?> paymentStatus(@RequestParam Map<String, String> allParams) throws RazorpayException, NumberFormatException, BookingNotFoundException {
        logger.info("paymentStatus in controller running");
        String linkStatus = allParams.get("razorpay_payment_link_status");
        if (!"paid".equalsIgnoreCase(linkStatus)) {
            logger.info("payment failed linkStatus {}", linkStatus);
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Payment not completed",
                "linkStatus", linkStatus
            ));
        }

        String paymentId = allParams.get("razorpay_payment_id");
        Payment payment = razorpayClient.payments.fetch(paymentId);

        logger.info("payment paid linkStatus {}", linkStatus);
        Long bookingId = Long.parseLong(allParams.get("bookingId"));
        service.changeStatus(paymentId, bookingId, linkStatus);
        logger.info("changeStatus running in controller successfully");
        return ResponseEntity.ok(Map.of(
            "paymentId", paymentId,
            "status", "fully_processed",
            "message", "Payment captured"
        ));
    }

    @GetMapping("/make/{bookingId}")
    public ResponseEntity<?> makePaymentLink(@PathVariable Long bookingId) {
        logger.info("Generating payment link for bookingId {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElse(null);
        if (booking == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Booking not found"));
        }
        try {
            String paymentLink = service.generatePaymentLink(booking);
            return ResponseEntity.ok(Map.of("paymentLink", paymentLink));
        } catch (Exception e) {
            logger.error("Error generating payment link: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to generate payment link"));
        }
    }
}
