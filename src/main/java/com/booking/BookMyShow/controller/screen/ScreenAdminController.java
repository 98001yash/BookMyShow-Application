package com.booking.BookMyShow.controller.screen;

import com.booking.BookMyShow.dtos.screen.CreateScreenRequestDto;
import com.booking.BookMyShow.dtos.screen.ScreenResponseDto;
import com.booking.BookMyShow.service.ScreenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/theatres/{theatreSlug}/screens")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class ScreenAdminController {

    private final ScreenService screenService;

    @PostMapping
    public ScreenResponseDto createScreen(
            @PathVariable String theatreSlug,
            @RequestBody CreateScreenRequestDto request) {

        log.info("Admin creating screen in theatre {}", theatreSlug);

        return screenService.createScreen(theatreSlug, request);
    }

    @PutMapping("/{screenSlug}")
    public ScreenResponseDto updateScreen(
            @PathVariable String theatreSlug,
            @PathVariable String screenSlug,
            @RequestBody CreateScreenRequestDto request) {

        log.info("Admin updating screen {} in theatre {}", screenSlug, theatreSlug);

        return screenService.updateScreen(theatreSlug, screenSlug, request);
    }

    @PatchMapping("/{screenSlug}/activate")
    public void activateScreen(
            @PathVariable String theatreSlug,
            @PathVariable String screenSlug) {

        log.info("Admin activating screen {}", screenSlug);

        screenService.activateScreen(theatreSlug, screenSlug);
    }

    @PatchMapping("/{screenSlug}/deactivate")
    public void deactivateScreen(
            @PathVariable String theatreSlug,
            @PathVariable String screenSlug) {

        log.info("Admin deactivating screen {}", screenSlug);

        screenService.deactivateScreen(theatreSlug, screenSlug);
    }

    @GetMapping
    public List<ScreenResponseDto> getAllScreensForAdmin(
            @PathVariable String theatreSlug) {

        log.info("Admin fetching all screens for theatre {}", theatreSlug);

        return screenService.getAllScreensForAdmin(theatreSlug);
    }
}