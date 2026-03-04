package com.booking.BookMyShow.repository;

import com.booking.BookMyShow.entity.ShowSeatPricing;
import com.booking.BookMyShow.enums.SeatTier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShowSeatPricingRepository extends JpaRepository<ShowSeatPricing, Long> {

    List<ShowSeatPricing> findByShowId(Long showId);

    Optional<ShowSeatPricing> findByShowIdAndTier(Long showId, SeatTier tier);
}
