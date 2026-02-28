package com.booking.BookMyShow.service;

import com.booking.BookMyShow.dtos.city.CityResponseDto;
import com.booking.BookMyShow.dtos.city.CreateCityRequest;
import com.booking.BookMyShow.dtos.city.PublicCityResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CityService {

    CityResponseDto createCity(CreateCityRequest request);

    CityResponseDto getCityById(Long id);

    Page<CityResponseDto> getAllCities(
            int page,
            int size,
            String sortBy,
            String direction
    );

    void activateCity(Long id);

    void deactivateCity(Long id);

    List<PublicCityResponse> getActiveCities();

    PublicCityResponse getCityBySlug(String slug);
}
