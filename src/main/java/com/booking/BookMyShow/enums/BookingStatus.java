package com.booking.BookMyShow.enums;

import java.util.EnumSet;



public enum BookingStatus {

    INITIATED,
    SEATS_LOCKED,
    PAYMENT_PENDING,
    CONFIRMED,
    PAYMENT_FAILED,
    EXPIRED,
    CANCELLED,
    REFUNDED,
    COMPLETED;

    public boolean canTransitionTo(BookingStatus nextStatus) {

        return switch (this) {

            case INITIATED -> EnumSet.of(SEATS_LOCKED, CANCELLED).contains(nextStatus);

            case SEATS_LOCKED -> EnumSet.of(PAYMENT_PENDING, EXPIRED).contains(nextStatus);

            case PAYMENT_PENDING -> EnumSet.of(CONFIRMED, PAYMENT_FAILED).contains(nextStatus);

            case PAYMENT_FAILED -> EnumSet.of(CANCELLED).contains(nextStatus);

            case CONFIRMED -> EnumSet.of(CANCELLED, REFUNDED, COMPLETED).contains(nextStatus);

            case CANCELLED -> EnumSet.of(REFUNDED).contains(nextStatus);

            case EXPIRED, REFUNDED, COMPLETED -> false;
        };

    }

    public boolean isTerminal() {
        return this == EXPIRED || this == REFUNDED || this == COMPLETED;
    }
}