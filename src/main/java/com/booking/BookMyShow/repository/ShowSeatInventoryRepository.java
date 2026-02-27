package com.booking.BookMyShow.repository;

import com.booking.BookMyShow.entity.ShowSeatInventory;
import com.booking.BookMyShow.enums.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShowSeatInventoryRepository
        extends JpaRepository<ShowSeatInventory, Long> {


    Optional<ShowSeatInventory> findByShowIdAndSeatNumber(
            Long showId,
            String seatNumber
    );

    List<ShowSeatInventory> findByShowIdAndStatus(
            Long showId,
            SeatStatus status
    );

    long countByShowIdAndStatus(Long showId, SeatStatus status);

    List<ShowSeatInventory> findByStatusAndLockedUntilBefore(
            SeatStatus status,
            LocalDateTime time
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ShowSeatInventory> findWithLockByShowIdAndSeatNumber(
            Long showId,
            String seatNumber
    );
}

