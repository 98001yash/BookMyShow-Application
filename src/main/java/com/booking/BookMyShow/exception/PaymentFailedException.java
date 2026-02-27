package com.booking.BookMyShow.exception;

import org.springframework.http.HttpStatus;

public class PaymentFailedException extends BaseException {

    public PaymentFailedException(String message) {
        super(message, ErrorCode.PAYMENT_FAILED, HttpStatus.BAD_REQUEST);
    }
}