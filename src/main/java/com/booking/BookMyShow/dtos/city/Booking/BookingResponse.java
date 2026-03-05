package com.booking.BookMyShow.dtos.city.Booking;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookingResponse {

    private Long bookingId;
    private String bookingReference;
    private Long showId;
    private List<String> seats;

    private Double totalAmount;
    private String status;
}
