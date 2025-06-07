package com.rms.bookingservice.feign;

import com.rms.bookingservice.dto.TrainDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "TRAIN-SERVICE")
public interface TrainServiceClient {

    @GetMapping("/train/{id}")
    TrainDto getTrainById(@PathVariable("id") Long id);

    @PutMapping("/train/update-seats/{id}")
    void updateAvailableSeats(@PathVariable("id") Long trainId, @RequestParam("availableSeats") int availableSeats);
}
