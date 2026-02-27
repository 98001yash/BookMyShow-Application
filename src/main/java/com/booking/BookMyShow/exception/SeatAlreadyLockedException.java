package com.booking.BookMyShow.exception;

import org.springframework.http.HttpStatus;

public class SeatAlreadyLockedException extends BaseException {

    public SeatAlreadyLockedException(String message) {
        super(message, ErrorCode.SEAT_ALREADY_LOCKED, HttpStatus.LOCKED);
    }
}