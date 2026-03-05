package com.booking.BookMyShow.controller;

import com.booking.BookMyShow.advice.ApiResponse;
import com.booking.BookMyShow.dtos.city.Booking.BookingResponse;
import com.booking.BookMyShow.dtos.city.Booking.CreateBookingRequest;
import com.booking.BookMyShow.service.BookingService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;


    @PostMapping
    public ApiResponse<BookingResponse> createBooking(
            @RequestBody CreateBookingRequest request
    ) {

        return ApiResponse.success(
                bookingService.createBooking(request),
                "Booking created successfully"
        );
    }


    @GetMapping("/{reference}")
    public ApiResponse<BookingResponse> getBooking(
            @PathVariable String reference
    ) {

        return ApiResponse.success(
                bookingService.getBooking(reference),
                "Booking fetched successfully"
        );
    }
}