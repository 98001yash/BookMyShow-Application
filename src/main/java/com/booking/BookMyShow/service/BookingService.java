package com.booking.BookMyShow.service;

import com.booking.BookMyShow.dtos.Booking.BookingResponse;
import com.booking.BookMyShow.dtos.Booking.CreateBookingRequest;

public interface BookingService {

    BookingResponse createBooking(CreateBookingRequest request);

    BookingResponse getBooking(String bookingReference);
}
