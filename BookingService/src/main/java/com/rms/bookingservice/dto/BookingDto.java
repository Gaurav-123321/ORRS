package com.rms.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long userId;
    private Long trainId;
    private int seatCount;
    private String paymentType;
    private String status;
    private String trainNumber;
    private String source;
    private String destination;
}