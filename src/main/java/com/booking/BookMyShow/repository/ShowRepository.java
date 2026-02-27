package com.booking.BookMyShow.repository;

import com.booking.BookMyShow.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByMovieIdAndShowTimeAfterAndActiveTrue(
            Long movieId,
            LocalDateTime time
    );

    List<Show> findByScreenTheatreIdAndShowTimeAfter(
            Long theatreId,
            LocalDateTime time
    );
}
