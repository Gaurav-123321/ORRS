package com.rms.bookingservice.exception;

public class BookingNotFoundException extends RuntimeException{
	public BookingNotFoundException(String message) {
		super(message);
	}
}
