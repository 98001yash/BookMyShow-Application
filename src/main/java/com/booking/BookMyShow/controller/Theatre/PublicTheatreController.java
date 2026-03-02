package com.booking.BookMyShow.controller.Theatre;

import com.booking.BookMyShow.dtos.Theatre.PublicTheatreResponse;
import com.booking.BookMyShow.dtos.Theatre.TheatreSummaryResponse;
import com.booking.BookMyShow.service.TheatreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/theatres")
@RequiredArgsConstructor
@Slf4j
public class PublicTheatreController {

    private final TheatreService theatreService;

    @GetMapping
    public Page<TheatreSummaryResponse> getTheatresByCity(
            @RequestParam Long cityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        log.info("Public API: Fetching theatres by cityId: {}", cityId);

        return theatreService.getTheatresByCity(
                cityId,
                page,
                size,
                sortBy,
                direction
        );
    }

    @GetMapping("/nearby")
    public List<PublicTheatreResponse> getNearbyTheatres(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "10") double radius
    ) {

        log.info("Public API: Fetching nearby theatres (lat={}, lng={}, radius={})",
                lat, lng, radius);

        return theatreService.getNearbyTheatres(
                lat,
                lng,
                radius
        );
    }
}