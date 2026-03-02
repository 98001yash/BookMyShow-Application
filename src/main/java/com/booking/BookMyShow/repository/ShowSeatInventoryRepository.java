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

    // 🔹 Find single seat by show + seat number (via SeatLayout relation)
    Optional<ShowSeatInventory> findByShowIdAndSeatLayout_SeatNumber(
            Long showId,
            String seatNumber
    );

    // 🔹 Fetch seats by show + status (AVAILABLE / LOCKED / BOOKED)
    List<ShowSeatInventory> findByShowIdAndStatus(
            Long showId,
            SeatStatus status
    );

    // 🔹 Count seats by status (useful for analytics / availability)
    long countByShowIdAndStatus(Long showId, SeatStatus status);

    // 🔹 Find expired locks
    List<ShowSeatInventory> findByStatusAndLockedUntilBefore(
            SeatStatus status,
            LocalDateTime time
    );

    // 🔹 Pessimistic lock version (for critical seat locking)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ShowSeatInventory> findWithLockByShowIdAndSeatLayout_SeatNumber(
            Long showId,
            String seatNumber
    );
}