package com.rms.userservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.rms.bookingservice.dto.BookingDto;
import com.rms.bookingservice.entity.Booking;

import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "BOOKING-SERVICE")
public interface BookingServiceClient {
    
    @PostMapping("/bookings/add")
    Booking addBooking(@RequestBody BookingDto bookingDto);

    @PostMapping("/bookings/cancel/{bookingId}/{userId}")
    void cancelBooking(@PathVariable Long bookingId, @PathVariable Long userId);
}

