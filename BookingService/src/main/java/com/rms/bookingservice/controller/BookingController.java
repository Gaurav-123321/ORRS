package com.rms.bookingservice.controller;

import com.rms.bookingservice.dto.BookingDto;
import com.rms.bookingservice.entity.Booking;
import com.rms.bookingservice.exception.BookingNotFoundException;
import com.rms.bookingservice.service.BookingService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/add")
    public ResponseEntity<?> bookTicket(@RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok(bookingService.add(bookingDto));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<String> payOrder(@PathVariable Long id) throws BookingNotFoundException, RazorpayException {
        String paymentLink = bookingService.makePayment(id);
        return ResponseEntity.ok("Complete your payment here: " + paymentLink);
    }

}