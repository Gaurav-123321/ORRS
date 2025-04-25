package com.rms.bookingservice.controller;

import org.springframework.web.bind.annotation.*;

import com.rms.bookingservice.dto.BookingDto;
import com.rms.bookingservice.entity.Booking;
import com.rms.bookingservice.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/add")
    public Booking addBooking(@RequestBody BookingDto bookingDto) {
        return bookingService.add(bookingDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.delete(id);
    }

    @GetMapping("/all")
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/user/{userId}")
    public Booking getBookingByUser(@PathVariable Long userId) {
        return bookingService.getBookingByUserId(userId);
    }
    
    @PutMapping("/update-status/{bookingId}/{status}")
    public void updateBookingStatus(@PathVariable Long bookingId, @PathVariable String status) {
        bookingService.updateBookingStatus(bookingId, status);
    }

    @PostMapping("/cancel/{bookingId}/{userId}")
    public String cancelBooking(@PathVariable Long bookingId, @PathVariable Long userId) {
        return bookingService.cancelTicket(bookingId, userId);
    }
}