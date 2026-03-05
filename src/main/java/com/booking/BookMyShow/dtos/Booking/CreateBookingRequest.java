package com.booking.BookMyShow.dtos.Booking;


import lombok.Data;

import java.util.List;

@Data
public class CreateBookingRequest {

    private Long showId;
    private List<String> seatNumbers;
    private Long userId;
    private String idempotencyKey;
}
