package com.tms.trainservice.service;

import java.util.List;

import com.tms.trainservice.entity.Train;

public interface TrainService {
	
	List<Train> get();
	Train getById(Long id);
	List<Train> getBySourceAndDestination(String source, String destination);
	Train add(Train train);
	void delete(Long id);
	void updateAvailableSeats(Long trainId, int availableSeats);

}
