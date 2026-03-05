package com.booking.BookMyShow.controller.show;


import com.booking.BookMyShow.dtos.Show.CreateShowRequestDto;
import com.booking.BookMyShow.dtos.Show.ShowResponseDto;
import com.booking.BookMyShow.service.ShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/shows")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class ShowAdminController {

    private final ShowService showService;

    @PostMapping
    public ShowResponseDto createShow(
            @Valid @RequestBody CreateShowRequestDto request
    ) {
        log.info("Admin creating show for movieId={} screenSlug={}",
                request.getMovieId(),
                request.getScreenSlug());
        return showService.createShow(request);
    }

    public ShowResponseDto getShowById(
            @PathVariable Long showId
    ){
        log.info("Fetching show with id={}",showId);
        return showService.getShowById(showId);
    }
    @GetMapping("/movie/{movieId}")
    public List<ShowResponseDto> getShowsByMovie(
            @PathVariable Long movieId
    ) {
        log.info("Fetching shows for movieId={}", movieId);
        return showService.getShowsByMovie(movieId);
    }

    @GetMapping("/screen/{screenId}")
    public List<ShowResponseDto> getShowsByScreen(
            @PathVariable Long screenId
    ) {
        log.info("Fetching shows for screenId={}", screenId);
        return showService.getShowsByScreen(screenId);
    }

    @PatchMapping("/{showId}/activate")
    public void activateShow(
            @PathVariable Long showId
    ) {
        log.info("Activating show {}", showId);
        showService.activateShow(showId);
    }

    @PatchMapping("/{showId}/deactivate")
    public void deactivateShow(
            @PathVariable Long showId
    ) {
        log.info("Deactivating show {}", showId);
        showService.deactivateShow(showId);
    }

}
