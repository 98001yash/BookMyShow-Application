package com.booking.BookMyShow.controller.Theatre;

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
    public TheatreResponseDto createTheatre(
            @Valid @RequestBody CreateTheatreRequest request
    ) {
        log.info("Admin creating theatre: {}", request.getName());
        return theatreService.createTheatre(request);
    }

    @PutMapping("/{id}")
    public TheatreResponseDto updateTheatre(
            @PathVariable Long id,
            @RequestBody UpdateTheatreRequest request
    ) {
        log.info("Admin updating theatre id: {}", id);
        return theatreService.updateTheatre(id, request);
    }

    @PatchMapping("/{id}/activate")
    public void activate(@PathVariable Long id) {
        log.info("Admin activating theatre id: {}", id);
        theatreService.activateTheatre(id);
    }

    @PatchMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        log.info("Admin deactivating theatre id: {}", id);
        theatreService.deactivateTheatre(id);
    }

    @GetMapping("/slug/{slug}")
    public TheatreResponseDto getBySlug(
            @PathVariable String slug
    ) {
        log.info("Admin fetching theatre by slug: {}", slug);
        return theatreService.getTheatreBySlug(slug);
    }

    @GetMapping
    public Page<TheatreSummaryResponse> getTheatresByCity(
            @RequestParam Long cityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        log.info("Admin fetching theatres by cityId: {}", cityId);

        return theatreService.getTheatresByCity(
                cityId,
                page,
                size,
                sortBy,
                direction
        );
    }
}