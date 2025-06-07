package com.rms.bookingservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Train ID is required")
    private Long trainId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Payment type is required")
    @Pattern(regexp = "^(CASH|CARD|UPI)$", message = "payment type should be from CASH, CARD, or UPI")
    private String paymentType;

    @NotNull(message = "Booking status is required")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private int noOfSeats;
    private double totalFare;
    private java.time.LocalDate journeyDate;

}
