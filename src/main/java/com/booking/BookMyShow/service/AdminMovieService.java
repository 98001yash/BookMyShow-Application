package com.booking.BookMyShow.service;

import com.booking.BookMyShow.dtos.Movies.CreateMovieRequest;
import com.booking.BookMyShow.dtos.Movies.MovieResponseDto;
import com.booking.BookMyShow.dtos.Movies.MovieSummaryResponse;
import com.booking.BookMyShow.dtos.Movies.UpdateMovieRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminMovieService {

    MovieResponseDto createMovie(CreateMovieRequest request);

    MovieResponseDto updateM0vie(Long movieId, UpdateMovieRequest request);

    void deactivateMovie(Long movieId);

    void activateMovie(Long movieId);

    MovieResponseDto getMovieById(Long movieId);

    Page<MovieSummaryResponse> getAllMovies(
            int page,
            int size,
            String sortBy,
            String direction
    );
}
