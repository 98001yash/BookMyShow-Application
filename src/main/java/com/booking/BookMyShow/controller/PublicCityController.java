package com.booking.BookMyShow.controller;


import com.booking.BookMyShow.advice.ApiResponse;
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
    public ApiResponse<List<PublicCityResponse>> getActiveCities() {

        log.info("Public API: Fetching active cities");

        return ApiResponse.success(
                cityService.getActiveCities()
        );
    }

    @GetMapping("/{slug}")
    public ApiResponse<PublicCityResponse> getCityBySlug(
            @PathVariable String slug
    ) {
        log.info("Public API: Fetching city by slug: {}", slug);

        return ApiResponse.success(
                cityService.getCityBySlug(slug)
        );
    }
}