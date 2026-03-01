package com.booking.BookMyShow.service;

import com.booking.BookMyShow.dtos.Theatre.CreateTheatreRequest;
import com.booking.BookMyShow.dtos.Theatre.TheatreResponseDto;
import com.booking.BookMyShow.dtos.Theatre.TheatreSummaryResponse;
import com.booking.BookMyShow.dtos.Theatre.UpdateTheatreRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TheatreService {

    TheatreResponseDto createTheatre(CreateTheatreRequest request);

    TheatreResponseDto updateTheatre(Long theatreId, UpdateTheatreRequest request);

    void activateTheatre(Long theatreId);

    void deactivateTheatre(Long theatreId);

    TheatreResponseDto getTheatreBySlug(String slug);

    Page<TheatreSummaryResponse> getTheatresByCity(Long cityId,
                                                   int page,
                                                   int size,
                                                   String sortBy,
                                                   String direction);

    List<TheatreSummaryResponse> getNearbyTheatres(
            double latitude,
            double longitude,
            double radius
    );
}
