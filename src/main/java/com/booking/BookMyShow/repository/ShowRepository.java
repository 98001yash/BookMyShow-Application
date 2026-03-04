package com.booking.BookMyShow.repository;

import com.booking.BookMyShow.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByMovieIdAndIsActiveTrue(Long movieId);

    List<Show> findByScreenIdAndIsActiveTrue(Long screenId);

    List<Show> findByScreenIdAndStartTimeBetween(
            Long screenId,
            LocalDateTime start,
            LocalDateTime end
    );

    // 🔥 Conflict detection
    @Query("""
        SELECT s FROM Show s
        WHERE s.screen.id = :screenId
        AND s.isActive = true
        AND (:startTime < s.endTime AND :endTime > s.startTime)
    """)
    List<Show> findConflictingShows(
            Long screenId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );
}