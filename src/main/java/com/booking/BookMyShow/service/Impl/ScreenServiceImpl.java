package com.booking.BookMyShow.service.Impl;


import com.booking.BookMyShow.dtos.screen.CreateScreenRequestDto;
import com.booking.BookMyShow.dtos.screen.ScreenResponseDto;
import com.booking.BookMyShow.entity.Screen;
import com.booking.BookMyShow.entity.SeatLayout;
import com.booking.BookMyShow.enums.SeatTier;
import com.booking.BookMyShow.repository.ScreenRepository;
import com.booking.BookMyShow.repository.SeatLayoutRepository;
import com.booking.BookMyShow.repository.TheatreRepository;
import com.booking.BookMyShow.service.ScreenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository screenRepository;
    private final TheatreRepository theatreRepository;
    private final SeatLayoutRepository seatLayoutRepository;


    @Override
    public ScreenResponseDto createScreen(String theatreSlug, CreateScreenRequestDto request) {
        return null;
    }

    @Override
    public ScreenResponseDto updateScreen(String theatreSlug, String screenSlug, CreateScreenRequestDto request) {
        return null;
    }

    @Override
    public void activateScreen(String theatreSlug, String screenSlug) {

    }

    @Override
    public void deactivateScreen(String theatreSlug, String screenSlug) {

    }

    @Override
    public List<ScreenResponseDto> getActiveScreens(String theatreSlug) {
        return List.of();
    }

    @Override
    public List<ScreenResponseDto> getAllScreensForAdmin(String theatreSlug) {
        return List.of();
    }


    private void generateSeatLayout(Screen screen, int totalRows, int seatsPerRow) {

        log.info("Generating seat layout for screen ID: {}", screen.getId());

        List<SeatLayout> seats = new ArrayList<>();

        for (int row = 0; row < totalRows; row++) {

            char rowChar = (char) ('A' + row);
            SeatTier tier = determineTier(row, totalRows);

            for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {

                String seatNumber = rowChar + String.valueOf(seatNum);

                seats.add(
                        SeatLayout.builder()
                                .seatNumber(seatNumber)
                                .rowLabel(String.valueOf(rowChar))
                                .tier(tier)
                                .screen(screen)
                                .build()
                );
            }
        }
        seatLayoutRepository.saveAll(seats);
        log.info("Generated {} seats for screen ID {}", seats.size(), screen.getId());
    }

    private SeatTier determineTier(int rowIndex, int totalRows) {

        int silverLimit = totalRows / 3;
        int goldLimit = (2 * totalRows) / 3;

        if (rowIndex < silverLimit) {
            return SeatTier.SILVER;
        } else if (rowIndex < goldLimit) {
            return SeatTier.GOLD;
        } else {
            return SeatTier.PLATINUM;
        }
    }

    private String generateSlug(String input) {
        return input.toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");
    }

    private ScreenResponseDto mapToResponse(Screen screen) {

        return ScreenResponseDto.builder()
                .name(screen.getName())
                .slug(screen.getSlug())
                .screenType(screen.getScreenType())
                .totalRows(screen.getTotalRows())
                .totalSeats(screen.getTotalSeats())
                .isActive(screen.getIsActive())
                .build();
    }
}
