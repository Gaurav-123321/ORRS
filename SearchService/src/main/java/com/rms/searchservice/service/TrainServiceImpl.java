package com.rms.searchservice.service;

import org.springframework.stereotype.Service;

import com.rms.searchservice.model.Train;
import com.rms.searchservice.repository.TrainRepository;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class TrainServiceImpl implements TrainService {
    private final TrainRepository trainRepository;

    public TrainServiceImpl(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Train addTrain(Train train) {
        return trainRepository.save(train);
    }

    @Override
    public void removeTrain(Long trainId) {
        trainRepository.deleteById(trainId);
    }

    @Override
    public List<Train> searchTrains(String source, String destination, LocalDateTime startDate, LocalDateTime endDate) {
        return trainRepository.findBySourceAndDestinationAndDepartureTimeBetween(source, destination, startDate, endDate);
    }
    
    public List<Train> loadAllTrains() {
        return trainRepository.findAll();
    }

}
