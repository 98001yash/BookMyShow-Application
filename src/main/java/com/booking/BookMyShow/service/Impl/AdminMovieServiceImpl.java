package com.booking.BookMyShow.service.Impl;

import com.booking.BookMyShow.dtos.Movies.CreateMovieRequest;
import com.booking.BookMyShow.dtos.Movies.MovieResponseDto;
import com.booking.BookMyShow.dtos.Movies.MovieSummaryResponse;
import com.booking.BookMyShow.dtos.Movies.UpdateMovieRequest;
import com.booking.BookMyShow.entity.Movie;
import com.booking.BookMyShow.exception.ResourceAlreadyExistsException;
import com.booking.BookMyShow.exception.ResourceNotFoundException;
import com.booking.BookMyShow.repository.MovieRepository;
import com.booking.BookMyShow.service.AdminMovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

            throw new ResourceAlreadyExistsException("Movie already exists with same title and language");
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


        log.info("Admin attempting to update movie id: {}",movieId);

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() ->{
                    log.warn("Movie not found with id: {}",movieId);
                    return new ResourceNotFoundException("Movie not found");
                });

        if (request.getTitle() != null) {
            movie.setTitle(request.getTitle());
        }

        if (request.getLanguage() != null) {
            movie.setLanguage(request.getLanguage());
        }

        if (request.getDurationMinutes() != null) {
            movie.setDurationMinutes(request.getDurationMinutes());
        }

        if (request.getGenre() != null) {
            movie.setGenre(request.getGenre());
        }

        if (request.getReleaseDate() != null) {
            movie.setReleaseDate(request.getReleaseDate());
        }

        Movie updated = movieRepository.save(movie);
        log.info("Movie updated successfully id: {}", movieId);
        return mapToResponse(updated);
    }

    @Override
    public void deactivateMovie(Long movieId) {

        log.info("Admin attempting to deactivate movie Id: {}",movieId);

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()-> {
                    log.warn("Movie not found for deactivation id: {}",movieId);
                    return new ResourceNotFoundException("Movie not found");
                });

                movie.setActive(false);
                movieRepository.save(movie);

                log.info("Movie deactivated id: {}",movieId);
    }

    @Override
    public void activateMovie(Long movieId) {
     log.info("Admin attempting to activate movie Id: {}",movieId);

     Movie movie = movieRepository.findById(movieId)
             .orElseThrow(()-> {
                 log.warn("Movie not found for activation id: {}",movieId);
                 return new ResourceNotFoundException("Movie not found");
             });
     movie.setActive(true);
     movieRepository.save(movie);

     log.info("Movie activated id: {}",movieId);
    }

    @Override
    public MovieResponseDto getMovieById(Long movieId) {

        log.info("Fetching movie with id: {}",movieId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()-> {
                    log.warn("Movie not found id: {}",movieId);
                    return new ResourceNotFoundException("Movie not found");
                });
        return mapToResponse(movie);
    }

    @Override
    public Page<MovieSummaryResponse> getAllMovies(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        log.info("Fetching movies - page: {}, size: {}, sortBy: {}, direction: {}",
                page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Movie> moviePage = movieRepository.findAll(pageable);

        return moviePage.map(movie ->
                MovieSummaryResponse.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .language(movie.getLanguage())
                        .active(movie.getActive())
                        .build()
        );
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
