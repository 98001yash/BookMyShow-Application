package com.booking.BookMyShow.exception;

import org.springframework.http.HttpStatus;

public class SeatAlreadyBookedException extends BaseException {

    public SeatAlreadyBookedException(String message) {
        super(message, ErrorCode.SEAT_ALREADY_BOOKED, HttpStatus.CONFLICT);
    }
}