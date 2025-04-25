package com.rms.searchservice.service;

import java.time.LocalDateTime;
import java.util.List;

import com.rms.searchservice.model.Train;

public interface TrainService {
    Train addTrain(Train train);
    void removeTrain(Long trainId);
    List<Train> searchTrains(String source, String destination, LocalDateTime startDate, LocalDateTime endDate);
	List<Train> loadAllTrains();
}
