package com.booking.BookMyShow.utils;

import com.booking.BookMyShow.service.SeatLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeatLockScheduler {

    private final SeatLockService seatLockService;

    @Scheduled(fixedRate = 30000) // every 30 seconds
    public void releaseExpiredSeatLocks() {

        log.info("Running seat lock cleanup job");
        seatLockService.releaseExpiredLocks();
    }
}