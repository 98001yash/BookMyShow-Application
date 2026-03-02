package com.booking.BookMyShow.controller.city;

import com.booking.BookMyShow.dtos.city.CityResponseDto;
import com.booking.BookMyShow.dtos.city.CreateCityRequest;
import com.booking.BookMyShow.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/cities")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminCityController {

    private final CityService cityService;

    @PostMapping
    public CityResponseDto createCity(
            @Valid @RequestBody CreateCityRequest request
    ) {
        log.info("Admin API: Create City");
        return cityService.createCity(request);
    }

    // -----------------------------------

    @GetMapping("/{id}")
    public CityResponseDto getCityById(
            @PathVariable Long id
    ) {
        log.info("Admin API: Get City By ID: {}", id);
        return cityService.getCityById(id);
    }

    @GetMapping
    public Page<CityResponseDto> getAllCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Admin API: Get All Cities");
        return cityService.getAllCities(page, size, sortBy, direction);
    }

    @PutMapping("/{id}/activate")
    public void activateCity(
            @PathVariable Long id
    ) {
        log.info("Admin API: Activate City: {}", id);
        cityService.activateCity(id);
    }

    @PutMapping("/{id}/deactivate")
    public void deactivateCity(
            @PathVariable Long id
    ) {
        log.info("Admin API: Deactivate City: {}", id);
        cityService.deactivateCity(id);
    }
}