package com.booking.BookMyShow.controller.city;

import com.booking.BookMyShow.dtos.city.PublicCityResponse;
import com.booking.BookMyShow.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
@Slf4j
public class PublicCityController {

    private final CityService cityService;

    @GetMapping
    public List<PublicCityResponse> getActiveCities() {

        log.info("Public API: Fetching active cities");

        return cityService.getActiveCities();
    }

    @GetMapping("/{slug}")
    public PublicCityResponse getCityBySlug(
            @PathVariable String slug
    ) {
        log.info("Public API: Fetching city by slug: {}", slug);

        return cityService.getCityBySlug(slug);
    }
}