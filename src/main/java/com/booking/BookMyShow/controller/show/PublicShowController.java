package com.booking.BookMyShow.controller.show;

import com.booking.BookMyShow.dtos.Show.ShowResponseDto;
import com.booking.BookMyShow.service.ShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
@Slf4j
public class PublicShowController {

    private final ShowService showService;

    @GetMapping("/movie/{movieId}")
    public List<ShowResponseDto> getShowsByMovie(
            @PathVariable Long movieId
    ) {
        log.info("Public API fetching shows for movieId={}", movieId);
        return showService.getShowsByMovie(movieId);
    }

    @GetMapping("/screen/{screenId}")
    public List<ShowResponseDto> getShowsByScreen(
            @PathVariable Long screenId
    ) {
        log.info("Public API fetching shows for screenId={}", screenId);
        return showService.getShowsByScreen(screenId);
    }

    @GetMapping("/{showId}")
    public ShowResponseDto getShowById(
            @PathVariable Long showId
    ) {
        log.info("Public API fetching show {}", showId);
        return showService.getShowById(showId);
    }
}
