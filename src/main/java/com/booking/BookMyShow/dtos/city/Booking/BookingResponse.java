package com.booking.BookMyShow.dtos.city.Booking;

import java.util.List;

public class BookingResponse {

    private Long bookingId;
    private String bookingResponse;
    private Long showId;
    private List<String> seats;

    private Double totalAmount;
    private String status;
}
