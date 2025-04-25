package com.rms.userservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SEARCH-SERVICE")
public interface SearchServiceClient {
    @PostMapping("/train/updateSeats/{trainId}/{seats}")
    void updateSeats(@PathVariable Long trainId, @PathVariable int seats);
}
