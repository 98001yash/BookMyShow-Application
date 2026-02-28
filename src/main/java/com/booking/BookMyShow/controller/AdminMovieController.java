package com.booking.BookMyShow.controller;


import com.booking.BookMyShow.dtos.Movies.CreateMovieRequest;
import com.booking.BookMyShow.dtos.Movies.MovieResponseDto;
import com.booking.BookMyShow.dtos.Movies.MovieSummaryResponse;
import com.booking.BookMyShow.dtos.Movies.UpdateMovieRequest;
import com.booking.BookMyShow.service.AdminMovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/movies")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminMovieController {

    private final AdminMovieService adminMovieService;

    @PostMapping
    public MovieResponseDto createMovie(@Valid @RequestBody CreateMovieRequest request){
        log.info("Received request to create movie: {}",request.getTitle());
        return adminMovieService.createMovie(request);
    }


    @PutMapping("/{id}")
    public MovieResponseDto updateMovie(@PathVariable Long id,
                                        @Valid @RequestBody UpdateMovieRequest request){

        log.info("Received request to update movie id: {}",id);
        return adminMovieService.updateM0vie(id, request);
    }



    @PatchMapping("/{id}/activate")
    public void activateMovie(@PathVariable Long id){
        log.info("Reqceived request to activate movie id: {}",id);
        adminMovieService.activateMovie(id);
    }



    @PatchMapping("/{id}/deactivate")
    public void deactivateMovie(@PathVariable Long id) {
        log.info("Received request to deactivate movie id: {}", id);
        adminMovieService.deactivateMovie(id);
    }

    @GetMapping("/{id}")
    public MovieResponseDto getMovieById(@PathVariable Long id) {
        log.info("Received request to fetch movie id: {}", id);
        return adminMovieService.getMovieById(id);
    }



    @GetMapping
    public Page<MovieSummaryResponse> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Received request to fetch paginated movies");
        return adminMovieService.getAllMovies(page, size, sortBy, direction);
    }
}
