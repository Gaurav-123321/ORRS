package com.rms.bookingservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.rms.bookingservice.dto.BookingDto;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {
    
    @GetMapping("/user/id/{userId}")
    BookingDto getUserById(@PathVariable Long userId);
}

