package com.rms.searchservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rms.searchservice.model.Train;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    List<Train> findBySourceAndDestinationAndDepartureTimeBetween(
            String source, String destination, LocalDateTime startDate, LocalDateTime endDate);
}
