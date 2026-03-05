package com.booking.BookMyShow.service;

import com.booking.BookMyShow.dtos.SeatLockRequest;
import com.booking.BookMyShow.dtos.SeatLockResponse;
import com.booking.BookMyShow.dtos.SeatStatusResponse;

import java.util.List;

public interface SeatLockService {

   //  Lock selected seats for a show
    SeatLockResponse lockSeats(Long showId, SeatLockRequest request);


    // Unlock seats manually (used if booking fails)
    void unlockSeats(Long showId, List<String> seatNumbers);

    // Fetch seat map with status
    List<SeatStatusResponse> getSeatMap(Long showId);


     //Release expired locks (used by scheduler)
    void releaseExpiredLocks();
}
