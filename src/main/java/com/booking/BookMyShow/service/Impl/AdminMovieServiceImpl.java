package com.booking.BookMyShow.service.Impl;

import com.booking.BookMyShow.dtos.Movies.CreateMovieRequest;
import com.booking.BookMyShow.dtos.Movies.MovieResponseDto;
import com.booking.BookMyShow.dtos.Movies.UpdateMovieRequest;
import com.booking.BookMyShow.entity.Movie;
import com.booking.BookMyShow.repository.MovieRepository;
import com.booking.BookMyShow.service.AdminMovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMovieServiceImpl implements AdminMovieService {

    private final MovieRepository movieRepository;
    @Override
    public MovieResponseDto createMovie(CreateMovieRequest request) {

        log.info("Admin attempting to create a movie: {} - {}",
                request.getTitle(), request.getLanguage());

        boolean exists = movieRepository
                .existsByTitleIgnoreCaseAndLanguageIgnoreCaseAndActiveTrue(
                        request.getTitle(),
                        request.getLanguage()
                );

        if(exists){
            log.warn("Duplicate movie creation attempt: {} - {}",
                    request.getTitle(), request.getLanguage());

            throw new RuntimeException("Movie already exists with same title and language");
        }

        Movie movie = Movie.builder()
                .title(request.getTitle())
                .language(request.getLanguage())
                .durationMinutes(request.getDurationMinutes())
                .genre(request.getGenre())
                .releaseDate(request.getReleaseDate())
                .active(true)
                .build();

        Movie saved = movieRepository.save(movie);
        log.info("Movie created successfully eith id: {}",saved.getId());
        return mapToResponse(saved);
    }

    @Override
    public MovieResponseDto updateM0vie(Long movieId, UpdateMovieRequest request) {
        return null;
    }

    @Override
    public void deactivateMovie(Long movieId) {

    }

    @Override
    public void activateMovie(Long movieId) {

    }

    @Override
    public MovieResponseDto getMovieById(Long movieId) {
        return null;
    }

    @Override
    public List<MovieResponseDto> getAllMovies() {
        return List.of();
    }



    private MovieResponseDto mapToResponse(Movie movie) {

        return MovieResponseDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .language(movie.getLanguage())
                .durationMinutes(movie.getDurationMinutes())
                .genre(movie.getGenre())
                .releaseDate(movie.getReleaseDate())
                .active(movie.getActive())
                .build();
    }
}
