package com.booking.BookMyShow.exception;

public enum ErrorCode {

    // Generic
    INTERNAL_ERROR,
    VALIDATION_FAILED,
    RESOURCE_NOT_FOUND,
    UNAUTHORIZED,
    ACCESS_DENIED,

    // Seat
    SEAT_ALREADY_LOCKED,
    SEAT_ALREADY_BOOKED,
    LOCK_EXPIRED,

    // Booking
    BOOKING_CONFLICT,
    BOOKING_NOT_FOUND,

    // Payment
    PAYMENT_FAILED,
    PAYMENT_TIMEOUT,
    PAYMENT_VERIFICATION_FAILED,

    // Pricing
    PRICE_CHANGED,

    // Rate limiting
    RATE_LIMIT_EXCEEDED
}