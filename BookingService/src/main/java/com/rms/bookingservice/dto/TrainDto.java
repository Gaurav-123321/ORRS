package com.rms.bookingservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainDto {
    private Long id;
    private String trainName;
    private String source;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double duration;
    private double price;
    private int availableSeats;
}
