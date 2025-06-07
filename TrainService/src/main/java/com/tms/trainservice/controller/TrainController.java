package com.tms.trainservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tms.trainservice.entity.Train;
import com.tms.trainservice.service.TrainService;

@CrossOrigin("*")
@RestController
@RequestMapping("/train")
public class TrainController {

    @Autowired
    TrainService trainService;

    @GetMapping("/all")
    public ResponseEntity<List<Train>> getAll() {
        return ResponseEntity.ok(trainService.get());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Train> getById(@PathVariable Long id) {
        return ResponseEntity.ok(trainService.getById(id));
    }

    @GetMapping("/source-destination/{source}/{destination}")
    public ResponseEntity<List<Train>> getBySourceAndDestination(@PathVariable String source, @PathVariable String destination) {
        return ResponseEntity.ok(trainService.getBySourceAndDestination(source, destination));
    }

    @PostMapping("/add")
    public ResponseEntity<Train> add(@RequestBody Train train) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainService.add(train));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        trainService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //  Update available seats
    @PutMapping("/update-seats/{id}")
    public ResponseEntity<Void> updateAvailableSeats(
            @PathVariable Long id,
            @RequestParam int availableSeats) {

        trainService.updateAvailableSeats(id, availableSeats);
        return ResponseEntity.ok().build();
    }
}
