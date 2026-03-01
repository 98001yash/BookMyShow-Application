package com.booking.BookMyShow.repository;

import com.booking.BookMyShow.entity.Theatre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TheatreRepository extends JpaRepository<Theatre,Long> {

    // Duplicate check (same name inside city
    boolean existsByNameIgnoreCaseAndCity_Id(
            String name,
            Long cityId
    );

    // Slug lookup (for routing)
    Optional<Theatre> findBySlugAndActiveTrue(String slug);

    // City based filtering (ADMIN)
    Page<Theatre> findAllByCity_Id(
            Long cityId,
            Pageable pageable
    );

    // Public city-based filtering
    Page<Theatre> findAllByCity_IdAndActiveTrue(
            Long cityId,
            Pageable pageable
    );

    // Simple list for internal use
    List<Theatre> findAllByCity_IdAndActiveTrue(Long cityId);


    // Geo-distance based theatre search
    @Query(value = """
    SELECT * FROM (
        SELECT t.*,
        (
            6371 * acos(
              LEAST(1.0,
                  cos(radians(:lat)) *
                  cos(radians(t.latitude)) *
                  cos(radians(t.longitude) - radians(:lng)) +
                  sin(radians(:lat)) *
                  sin(radians(t.latitude))
              )
          ) AS distance
        FROM theatres t
        WHERE t.active = true
    ) AS calculated
    WHERE calculated.distance <= :radius
    ORDER BY calculated.distance
    """,
            nativeQuery = true)
    List<Theatre> findNearbyTheatres(
            @Param("lat") double latitude,
            @Param("lng") double longitude,
            @Param("radius") double radius
    );
}
