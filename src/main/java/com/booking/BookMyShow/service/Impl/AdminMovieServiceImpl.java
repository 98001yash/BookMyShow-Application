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

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMovieServiceImpl implements AdminMovieService {

    private final MovieRepository movieRepository;

    @Override
    public MovieResponseDto createMovie(CreateMovieRequest request) {

        log.info("Admin creating movie: {} | Language: {}",
                request.getTitle(), request.getLanguage());

        boolean exists = movieRepository
                .existsByTitleIgnoreCaseAndLanguageIgnoreCaseAndActiveTrue(
                        request.getTitle(),
                        request.getLanguage()
                );

        if (exists) {
            log.warn("Duplicate movie creation blocked: {} - {}",
                    request.getTitle(), request.getLanguage());

            throw new ResourceAlreadyExistsException(
                    "Active movie already exists with same title and language"
            );
        }

        Movie movie = Movie.builder()
                .title(request.getTitle().trim())
                .language(request.getLanguage().trim())
                .durationMinutes(request.getDurationMinutes())
                .genre(request.getGenre())
                .releaseDate(request.getReleaseDate())
                .description(request.getDescription())
                .posterUrl(request.getPosterUrl())
                .bannerImageUrl(request.getBannerImageUrl())
                .trailerUrl(request.getTrailerUrl())
                .certification(request.getCertification())
                .imdbId(request.getImdbId())
                .rating(request.getRating() != null ? request.getRating() : 0.0)
                .popularityScore(0)
                .featured(Boolean.TRUE.equals(request.getFeatured()))
                .active(true)
                .build();

        Movie saved = movieRepository.save(movie);

        log.info("Movie created successfully with ID: {}", saved.getId());

        return mapToResponse(saved);
    }

    @Override
    public MovieResponseDto updateM0vie(Long movieId, UpdateMovieRequest request) {
        log.info("Admin updating movie ID: {}", movieId);

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> {
                    log.warn("Movie not found for update: {}", movieId);
                    return new ResourceNotFoundException("Movie not found");
                });

        // Prevent duplicate title+language combination
        if (request.getTitle() != null && request.getLanguage() != null) {
            boolean exists = movieRepository
                    .existsByTitleIgnoreCaseAndLanguageIgnoreCaseAndActiveTrue(
                            request.getTitle(),
                            request.getLanguage()
                    );

            if (exists &&
                    !(movie.getTitle().equalsIgnoreCase(request.getTitle())
                            && movie.getLanguage().equalsIgnoreCase(request.getLanguage()))) {

                throw new ResourceAlreadyExistsException(
                        "Another movie already exists with same title and language"
                );
            }
        }

        // Partial updates
        if (request.getTitle() != null) movie.setTitle(request.getTitle().trim());
        if (request.getLanguage() != null) movie.setLanguage(request.getLanguage().trim());
        if (request.getDurationMinutes() != null) movie.setDurationMinutes(request.getDurationMinutes());
        if (request.getGenre() != null) movie.setGenre(request.getGenre());
        if (request.getReleaseDate() != null) movie.setReleaseDate(request.getReleaseDate());
        if (request.getDescription() != null) movie.setDescription(request.getDescription());
        if (request.getPosterUrl() != null) movie.setPosterUrl(request.getPosterUrl());
        if (request.getBannerImageUrl() != null) movie.setBannerImageUrl(request.getBannerImageUrl());
        if (request.getTrailerUrl() != null) movie.setTrailerUrl(request.getTrailerUrl());
        if (request.getCertification() != null) movie.setCertification(request.getCertification());
        if (request.getRating() != null) movie.setRating(request.getRating());
        if (request.getPopularityScore() != null) movie.setPopularityScore(request.getPopularityScore());
        if (request.getFeatured() != null) movie.setFeatured(request.getFeatured());
        if (request.getActive() != null) movie.setActive(request.getActive());

        Movie updated = movieRepository.save(movie);

        log.info("Movie updated successfully ID: {}", movieId);

        return mapToResponse(updated);
    }


    @Override
    public void deactivateMovie(Long movieId) {

        log.info("Deactivating movie ID: {}", movieId);

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        movie.setActive(false);
        movieRepository.save(movie);
    }

    @Override
    public void activateMovie(Long movieId) {

        log.info("Activating movie ID: {}", movieId);

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        movie.setActive(true);
        movieRepository.save(movie);
    }

    @Override
    public MovieResponseDto getMovieById(Long movieId) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        return mapToResponse(movie);
    }

    @Override
    public Page<MovieSummaryResponse> getAllMovies(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        List<String> allowedSortFields = List.of(
                "id",
                "title",
                "releaseDate",
                "rating",
                "popularityScore",
                "createdAt"
        );

        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "id";
        }

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
                        .posterUrl(movie.getPosterUrl())
                        .rating(movie.getRating())
                        .certification(
                                movie.getCertification() != null
                                        ? movie.getCertification().name()
                                        : null
                        )
                        .featured(movie.getFeatured())
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
                .description(movie.getDescription())
                .posterUrl(movie.getPosterUrl())
                .bannerImageUrl(movie.getBannerImageUrl())
                .trailerUrl(movie.getTrailerUrl())
                .certification(movie.getCertification())
                .imdbId(movie.getImdbId())
                .rating(movie.getRating())
                .popularityScore(movie.getPopularityScore())
                .featured(movie.getFeatured())
                .active(movie.getActive())
                .createdAt(movie.getCreatedAt())
                .updatedAt(movie.getUpdatedAt())
                .build();
    }
}