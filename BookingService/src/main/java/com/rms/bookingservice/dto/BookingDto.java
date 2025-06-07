package com.rms.bookingservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long userId;
    private Long trainId;
    private LocalDate journeyDate;
    private String paymentType;
    @JsonAlias({ "numberOfSeats", "noOfSeats" })
    private int noOfSeats;
    
}
