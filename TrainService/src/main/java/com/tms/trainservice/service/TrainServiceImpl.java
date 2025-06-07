package com.tms.trainservice.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tms.trainservice.entity.Train;
import com.tms.trainservice.exception.TrainNotFoundException;
import com.tms.trainservice.repository.TrainRepository;

@Service
public class TrainServiceImpl implements TrainService {

    private static final Logger logger = LoggerFactory.getLogger(TrainServiceImpl.class);

    @Autowired
    TrainRepository trainRepo;

    @Override
    public List<Train> get() {
        logger.info("Fetching all trains");
        List<Train> trains = trainRepo.findAll();
        logger.info("Total trains found: {}", trains.size());
        return trains;
    }

    @Override
    public Train getById(Long id) {
        logger.info("Fetching train with ID: {}", id);
        Train train = trainRepo.findById(id).orElseThrow(() -> {
            logger.error("Train with ID {} not found", id);
            return new TrainNotFoundException("Train not found with ID: " + id);
        });
        logger.info("Train found: {}", train);
        return train;
    }

    @Override
    public List<Train> getBySourceAndDestination(String source, String destination) {
        logger.info("Fetching trains from {} to {}", source, destination);
        List<Train> trains = trainRepo.findBySourceAndDestination(source, destination);
        if (trains.isEmpty()) {
            logger.error("No trains found from {} to {}", source, destination);
            throw new TrainNotFoundException("No trains found from " + source + " to " + destination);
        }
        logger.info("Total trains found: {}", trains.size());
        return trains;
    }

    @Override
    public Train add(Train train) {
        logger.info("Adding new train: {}", train);
        Train savedTrain = trainRepo.save(train);
        logger.info("Train added successfully: {}", savedTrain);
        return savedTrain;
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting train with ID: {}", id);
        if (!trainRepo.existsById(id)) {
            logger.error("Cannot delete. Train with ID {} not found", id);
            throw new TrainNotFoundException("Cannot delete. Train with ID " + id + " not found.");
        }
        trainRepo.deleteById(id);
        logger.info("Train deleted successfully");
    }

    @Override
    public void updateAvailableSeats(Long trainId, int availableSeats) {
        logger.info("Updating available seats for train ID {}: {}", trainId, availableSeats);
        Train train = trainRepo.findById(trainId).orElseThrow(() -> {
            logger.error("Train with ID {} not found", trainId);
            return new RuntimeException("Train not found with ID: " + trainId);
        });
        int oldSeats = train.getAvailableSeats();
        train.setAvailableSeats(availableSeats);
        trainRepo.save(train);
        logger.info("Updated available seats for train ID {}: {} (was {})", trainId, availableSeats, oldSeats);
    }
}