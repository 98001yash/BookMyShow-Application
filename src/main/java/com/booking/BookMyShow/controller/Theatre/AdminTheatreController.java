package com.booking.BookMyShow.controller.Theatre;

import com.booking.BookMyShow.advice.ApiResponse;
import com.booking.BookMyShow.dtos.Theatre.CreateTheatreRequest;
import com.booking.BookMyShow.dtos.Theatre.TheatreResponseDto;
import com.booking.BookMyShow.dtos.Theatre.TheatreSummaryResponse;
import com.booking.BookMyShow.dtos.Theatre.UpdateTheatreRequest;
import com.booking.BookMyShow.service.TheatreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/theatres")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminTheatreController {

    private final TheatreService theatreService;


    @PostMapping
    public ApiResponse<TheatreResponseDto> createTheatre(
            @Valid @RequestBody CreateTheatreRequest request
    ) {
        log.info("Admin creating theatre: {}", request.getName());
        return ApiResponse.success(
                theatreService.createTheatre(request),
                response);
    }


    @PutMapping("/{id}")
    public ApiResponse<TheatreResponseDto> updateTheatre(
            @PathVariable Long id,
            @RequestBody UpdateTheatreRequest request
    ) {
        log.info("Admin updating theatre id: {}", id);
        return ApiResponse.success(
                theatreService.updateTheatre(id, request),
                response);
    }


    @PatchMapping("/{id}/activate")
    public ApiResponse<String> activate(@PathVariable Long id) {
        theatreService.activateTheatre(id);
        return ApiResponse.success("Theatre activated successfully", response);
    }


    @PatchMapping("/{id}/deactivate")
    public ApiResponse<String> deactivate(@PathVariable Long id) {
        theatreService.deactivateTheatre(id);
        return ApiResponse.success("Theatre deactivated successfully", response);
    }


    @GetMapping("/slug/{slug}")
    public ApiResponse<TheatreResponseDto> getBySlug(
            @PathVariable String slug
    ) {
        return ApiResponse.success(
                theatreService.getTheatreBySlug(slug),
                response);
    }


    @GetMapping
    public ApiResponse<Page<TheatreSummaryResponse>> getTheatresByCity(
            @RequestParam Long cityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        return ApiResponse.success(
                theatreService.getTheatresByCity(
                        cityId,
                        page,
                        size,
                        sortBy,
                        direction
                ),
                response);
    }
}