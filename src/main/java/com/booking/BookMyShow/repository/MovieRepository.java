package com.booking.BookMyShow.repository;

import com.booking.BookMyShow.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie,Long> {


    List<Movie> findByActiveTrue();

    List<Movie> findByTitleContainingIgnoreCase(String title);
}
