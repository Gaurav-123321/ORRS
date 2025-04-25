package com.rms.bookingservice.feign;

import com.rms.bookingservice.dto.BookingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SEARCH-SERVICE")
public interface SearchServiceClient {
    @GetMapping("/train/id/{trainId}")
    BookingDto getTrainById(@PathVariable Long trainId);
    
    void restoreSeats(Long trainId, int seatCount);
}
