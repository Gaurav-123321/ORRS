package com.tms.trainservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tms.trainservice.entity.Train;

public interface TrainRepository extends JpaRepository<Train, Long> {
    List<Train> findBySourceAndDestination(String source, String destination);
}
