package com.booking.BookMyShow.service.Impl;

import com.booking.BookMyShow.dtos.SeatLockRequest;
import com.booking.BookMyShow.dtos.SeatLockResponse;
import com.booking.BookMyShow.dtos.SeatStatusResponse;
import com.booking.BookMyShow.entity.ShowSeatInventory;
import com.booking.BookMyShow.enums.SeatStatus;
import com.booking.BookMyShow.repository.ShowSeatInventoryRepository;
import com.booking.BookMyShow.service.SeatLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatLockServiceImpl implements SeatLockService {

    private final ShowSeatInventoryRepository inventoryRepository;
    private static final int LOCK_DURATION_MINUTES = 5;


    @Override
    public SeatLockResponse lockSeats(Long showId, SeatLockRequest request) {


        LocalDateTime lockExpiry = LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES);
        List<String> lockedSeats = new ArrayList<>();

        for (String seatNumber : request.getSeatNumbers()) {

            ShowSeatInventory inventory = inventoryRepository
                    .findWithLockByShowIdAndSeatLayout_SeatNumber(showId, seatNumber)
                    .orElseThrow(() ->
                            new RuntimeException("Seat not found: " + seatNumber));

            if (inventory.getStatus() == SeatStatus.BOOKED) {
                throw new RuntimeException("Seat already booked: " + seatNumber);
            }

            if (inventory.getStatus() == SeatStatus.LOCKED &&
                    inventory.getLockedUntil() != null &&
                    inventory.getLockedUntil().isAfter(LocalDateTime.now())) {

                throw new RuntimeException("Seat already locked: " + seatNumber);
            }

            inventory.setStatus(SeatStatus.LOCKED);
            inventory.setLockedUntil(lockExpiry);

            lockedSeats.add(seatNumber);
        }

        log.info("Seats locked for show {} seats={}", showId, lockedSeats);

        return SeatLockResponse.builder()
                .showId(showId)
                .lockedSeats(lockedSeats)
                .lockedUntil(lockExpiry)
                .build();
    }

    @Override
    public void unlockSeats(Long showId, List<String> seatNumbers) {

    }

    @Override
    public List<SeatStatusResponse> getSeatMap(Long showId) {
        return List.of();
    }

    @Override
    public void releaseExpiredLocks() {

    }
}
