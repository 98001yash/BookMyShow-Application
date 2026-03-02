package com.booking.BookMyShow.service;

import com.booking.BookMyShow.dtos.screen.CreateScreenRequestDto;
import com.booking.BookMyShow.dtos.screen.ScreenResponseDto;

import java.util.List;

public interface ScreenService {

    ScreenResponseDto createScreen(String theatreSlug, CreateScreenRequestDto request);

    ScreenResponseDto updateScreen(String theatreSlug, String screenSlug, CreateScreenRequestDto request);

    void activateScreen(String theatreSlug, String screenSlug);

    void deactivateScreen(String theatreSlug, String screenSlug);

    List<ScreenResponseDto> getActiveScreens(String theatreSlug);
    List<ScreenResponseDto> getAllScreensForAdmin(String theatreSlug);
}
