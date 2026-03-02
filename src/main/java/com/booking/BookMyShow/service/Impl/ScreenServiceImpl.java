package com.booking.BookMyShow.service.Impl;


import com.booking.BookMyShow.dtos.screen.CreateScreenRequestDto;
import com.booking.BookMyShow.dtos.screen.ScreenResponseDto;
import com.booking.BookMyShow.entity.Screen;
import com.booking.BookMyShow.entity.SeatLayout;
import com.booking.BookMyShow.entity.Theatre;
import com.booking.BookMyShow.enums.SeatTier;
import com.booking.BookMyShow.exception.ResourceNotFoundException;
import com.booking.BookMyShow.repository.ScreenRepository;
import com.booking.BookMyShow.repository.SeatLayoutRepository;
import com.booking.BookMyShow.repository.TheatreRepository;
import com.booking.BookMyShow.service.ScreenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    @Transactional
    public ScreenResponseDto createScreen(String theatreSlug, CreateScreenRequestDto request) {

        log.info("Creating screen '{}' for theatre '{}'",request.getName(), theatreSlug);

        Theatre theatre = theatreRepository.findBySlug(theatreSlug)
                .orElseThrow(()-> {
                    log.error("Theatre not found with slug: {}",theatreSlug);
                    return new ResourceNotFoundException("Theatre not found");
                });

        if(!theatre.getActive()){
            log.warn("Attempt to create screen for inactive theatre: {}",theatreSlug);
            throw new RuntimeException("cannot create  screen fir inactive theatre");
        }

        if (screenRepository.existsByTheatreAndNameIgnoreCase(theatre, request.getName())) {
            log.warn("Duplicate screen name '{}' in theatre '{}'", request.getName(), theatreSlug);
            throw new RuntimeException("Screen with same name already exists in this theatre");
        }

        String slug = generateSlug(request.getName());

        if (screenRepository.existsByTheatreAndSlug(theatre, slug)) {
            log.warn("Duplicate screen slug '{}' in theatre '{}'", slug, theatreSlug);
            throw new RuntimeException("Screen slug conflict. Try different name.");
        }

        if (request.getTotalRows() == null || request.getTotalRows() <= 0) {
            throw new RuntimeException("Total rows must be greater than 0");
        }
        if (request.getSeatsPerRow() == null || request.getSeatsPerRow() <= 0) {
            throw new RuntimeException("Seats per row must be greater than 0");
        }

        int totalSeats = request.getTotalRows() * request.getSeatsPerRow();

        Screen screen = Screen.builder()
                .name(request.getName())
                .slug(slug)
                .screenType(request.getScreenType())
                .totalRows(request.getTotalRows())
                .totalSeats(totalSeats)
                .isActive(true)
                .theatre(theatre)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Screen savedScreen = screenRepository.save(screen);

        log.info("Screen saved with ID: {}", savedScreen.getId());

        generateSeatLayout(savedScreen, request.getTotalRows(), request.getSeatsPerRow());
        return mapToResponse(savedScreen);
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

        Theatre theatre = theatreRepository.findBySlug(theatreSlug)
                .orElseThrow(()->new ResourceNotFoundException("Theatre not found"));

        return screenRepository.findByTheatreAndIsActiveTrue(theatre)
                .stream()
                .map(this::mapToResponse)
                .toList();
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
