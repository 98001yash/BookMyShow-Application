package com.booking.BookMyShow.exception;

import org.springframework.http.HttpStatus;

public class PriceChangedException extends BaseException {

    public PriceChangedException(String message) {
        super(message, ErrorCode.PRICE_CHANGED, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}