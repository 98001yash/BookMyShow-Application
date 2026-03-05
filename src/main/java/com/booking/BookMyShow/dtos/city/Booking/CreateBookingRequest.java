package com.booking.BookMyShow.dtos.city.Booking;


import lombok.Data;

import java.util.List;

@Data
public class CreateBookingRequest {

    private Long showId;
    private List<String> seatNumbers;
}
