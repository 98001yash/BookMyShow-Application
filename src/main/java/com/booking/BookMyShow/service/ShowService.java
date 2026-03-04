package com.booking.BookMyShow.service;

import com.booking.BookMyShow.dtos.Show.CreateShowRequestDto;
import com.booking.BookMyShow.dtos.Show.ShowResponseDto;

import java.util.List;

public interface ShowService {

    ShowResponseDto createShow(CreateShowRequestDto request);

    // Fetch show by ID
    ShowResponseDto getShowById(Long showId);

    // Fetch all active shows for a movie
    List<ShowResponseDto> getShowsByMovie(Long movieId);

    // Fetch all active shows for a screen
    List<ShowResponseDto> getShowsByScreen(Long screenId);

    // Admin activates a show
    void activateShow(Long showId);

    // Admin deactivates a show
    void deactivateShow(Long showId);
}
