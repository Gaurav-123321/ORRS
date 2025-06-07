package com.ams.bookingservice;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rms.bookingservice.exception.BookingNotFoundException;
import com.rms.bookingservice.exception.ErrorResponse;
import com.rms.bookingservice.exception.GlobalExceptionHandler;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleBookingNotFoundException() {
        // Given
        BookingNotFoundException ex = new BookingNotFoundException("Booking not found");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBookingNotFoundException(ex);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Booking not found", response.getBody());
    }
}

