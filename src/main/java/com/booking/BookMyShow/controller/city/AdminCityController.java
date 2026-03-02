package com.booking.BookMyShow.controller.city;


import com.booking.BookMyShow.advice.ApiResponse;
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
    public ApiResponse<CityResponseDto> createCity(
            @Valid @RequestBody CreateCityRequest request
    ) {
        log.info("Admin API: Create City");
        CityResponseDto response = cityService.createCity(request);
        return ApiResponse.success(response, response);
    }

    // -----------------------------------
    @GetMapping("/{id}")
    public ApiResponse<CityResponseDto> getCityById(
            @PathVariable Long id
    ) {
        log.info("Admin API: Get City By ID: {}", id);
        CityResponseDto response = cityService.getCityById(id);
        return ApiResponse.success(response, response);
    }


    @GetMapping
    public ApiResponse<Page<CityResponseDto>> getAllCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        log.info("Admin API: Get All Cities");

        Page<CityResponseDto> response =
                cityService.getAllCities(page, size, sortBy, direction);
        return ApiResponse.success(response, response);
    }


    @PutMapping("/{id}/activate")
    public ApiResponse<String> activateCity(
            @PathVariable Long id
    ) {
        log.info("Admin API: Activate City: {}", id);
        cityService.activateCity(id);
        return ApiResponse.success("City activated successfully", response);
    }

    @PutMapping("/{id}/deactivate")
    public ApiResponse<String> deactivateCity(
            @PathVariable Long id
    ) {
        log.info("Admin API: Deactivate City: {}", id);
        cityService.deactivateCity(id);
        return ApiResponse.success("City deactivated successfully", response);
    }

}
