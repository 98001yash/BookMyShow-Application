package com.booking.BookMyShow.controller;


import com.booking.BookMyShow.dtos.SeatLockRequest;
import com.booking.BookMyShow.dtos.SeatLockResponse;
import com.booking.BookMyShow.dtos.SeatStatusResponse;
import com.booking.BookMyShow.service.SeatLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shows")
@RequiredArgsConstructor
public class SeatLockController {

    private final SeatLockService seatLockService;

    @PostMapping("/{showId}/lock-seats")
    public SeatLockResponse lockSeats(
            @PathVariable Long showId,
            @RequestBody SeatLockRequest request
    ) {
        return seatLockService.lockSeats(showId, request);
    }



    @PostMapping("/{showId}/unlock-seats")
    public void unlockSeats(
            @PathVariable Long showId,
            @RequestBody List<String> seatNumbers
    ) {
        seatLockService.unlockSeats(showId, seatNumbers);
    }

    @GetMapping("/{showId}/seats")
    public List<SeatStatusResponse> getSeatMap(
            @PathVariable Long showId
    ) {
        return seatLockService.getSeatMap(showId);
    }
}
