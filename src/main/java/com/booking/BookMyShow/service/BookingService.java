package com.booking.BookMyShow.service;

import com.booking.BookMyShow.dtos.city.Booking.BookingResponse;
import com.booking.BookMyShow.dtos.city.Booking.CreateBookingRequest;

public interface BookingService {

    BookingResponse createBooking(CreateBookingRequest request);

    BookingResponse getBooking(String bookingReference);
}
