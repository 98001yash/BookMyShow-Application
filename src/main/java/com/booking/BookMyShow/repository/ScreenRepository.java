package com.booking.BookMyShow.repository;

import com.booking.BookMyShow.entity.Screen;
import com.booking.BookMyShow.entity.Theatre;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ScreenRepository extends JpaRepository<Screen, Long> {

    boolean existsByTheatreAndNameIgnoreCase(Theatre theatre, String name);

    boolean existsByTheatreAndSlug(Theatre theatre, String slug);

    Optional<Screen> findByTheatreAndSlug(Theatre theatre, String slug);

    List<Screen> findByTheatreAndIsActiveTrue(Theatre theatre);


    Optional<Screen> findBySlug(@NotBlank(message = "Screen slug is required") String screenSlug);
}
