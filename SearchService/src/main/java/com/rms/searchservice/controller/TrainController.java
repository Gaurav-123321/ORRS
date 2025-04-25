package com.rms.searchservice.controller;

import org.springframework.web.bind.annotation.*;

import com.rms.searchservice.model.Train;
import com.rms.searchservice.service.TrainService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/trains")
public class TrainController {
    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @PostMapping("/add")
    public Train addTrain(@RequestBody Train train) {
        return trainService.addTrain(train);
    }

    @DeleteMapping("/remove/{trainId}")
    public void removeTrain(@PathVariable Long trainId) {
        trainService.removeTrain(trainId);
    }

    @GetMapping("/search")
    public List<Train> searchTrains(@RequestParam String source, @RequestParam String destination,
                                    @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return trainService.searchTrains(source, destination, startDate, endDate);
    }

    @GetMapping("/all")
    public List<Train> getAllTrains() {
        return trainService.loadAllTrains();
    }
}

