package com.tms.trainservice.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Train name cannot be empty")
    private String trainName;

    @NotBlank(message = "Source cannot be empty")
    private String source;

    @NotBlank(message = "Destination cannot be empty")
    private String destination;

    @NotNull(message = "Departure time is required")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalDateTime arrivalTime;

    @Min(value = 0, message = "Duration cannot be negative")
    private double duration;

    @Min(value = 0, message = "Price cannot be negative")
    private double price;

    @Min(value = 0, message = "Available seats cannot be negative")
    private int availableSeats;

    @Enumerated(EnumType.STRING)
    private TrainStatus status;

    @PrePersist
    public void setDefaultStatus() {
        if (this.status == null) {
            this.status = TrainStatus.getDefault();
        }
    }
}
