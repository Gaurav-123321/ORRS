package com.rms.bookingservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long trainId;
    private int seatCount;
    private String paymentType;
    private String status;
    private String trainNumber;
    private String source;
    private String destination;
    private boolean isCancelled = false;
}