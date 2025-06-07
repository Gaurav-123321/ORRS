package com.rms.bookingservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.rms.bookingservice.dto.UserDto;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {
    
    @GetMapping("/user/id/{id}")
    UserDto getUserById(@PathVariable Long id);
}
