package com.booking.BookMyShow.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends BaseException {
    public ResourceAlreadyExistsException(String message) {
        super(message, ErrorCode.RESOURCE_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }
}