package com.booking.BookMyShow.controller.screen;

import com.booking.BookMyShow.dtos.screen.ScreenResponseDto;
import com.booking.BookMyShow.service.ScreenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/theatres/{theatreSlug}/screens")
@RequiredArgsConstructor
@Slf4j
public class ScreenPublicController {

    private final ScreenService screenService;

    @GetMapping
    public List<ScreenResponseDto> getActiveScreens(
            @PathVariable String theatreSlug) {

        log.info("Fetching active screens for theatre {}", theatreSlug);

        return screenService.getActiveScreens(theatreSlug);
    }
}