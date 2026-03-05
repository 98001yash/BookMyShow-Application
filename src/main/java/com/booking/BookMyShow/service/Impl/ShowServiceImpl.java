package com.booking.BookMyShow.service.Impl;

import com.booking.BookMyShow.dtos.Show.CreateShowRequestDto;
import com.booking.BookMyShow.dtos.Show.ShowResponseDto;
import com.booking.BookMyShow.dtos.Show.ShowSeatPricingDto;
import com.booking.BookMyShow.entity.*;
import com.booking.BookMyShow.enums.SeatStatus;
import com.booking.BookMyShow.exception.ResourceNotFoundException;
import com.booking.BookMyShow.repository.*;
import com.booking.BookMyShow.service.ShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final SeatLayoutRepository seatLayoutRepository;
    private final ShowSeatPricingRepository pricingRepository;
    private final ShowSeatInventoryRepository inventoryRepository;

    @Override
    @Transactional
    public ShowResponseDto createShow(CreateShowRequestDto request) {


        log.info("Creating show for movieId={} screenSlug={}",
                request.getMovieId(),
                request.getScreenSlug());

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(()-> new ResourceNotFoundException("Movie not found"));

        Screen screen = screenRepository.findBySlug(request.getScreenSlug())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        if(!screen.getIsActive()) {
            throw new RuntimeException("Screen not active");
        }

        List<Show> conflicts = showRepository.findConflictingShows(
                screen.getId(),
                request.getStartTime(),
                request.getEndTime()
        );

        Show show = Show.builder()
                .movie(movie)
                .screen(screen)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .language(request.getLanguage())
                .format(request.getFormat())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        showRepository.save(show);

        log.info("Show created with id={}",show.getId());

        //  Save pricing
        List<ShowSeatPricing> pricing = request.getPricing()
                .stream()
                .map(p -> ShowSeatPricing.builder()
                        .show(show)
                        .tier(p.getTier())
                        .price(p.getPrice())
                        .build())
                .collect(Collectors.toList());

        pricingRepository.saveAll(pricing);

        log.info("Pricing created for show {}", show.getId());

        //  Generate seat inventory
        List<SeatLayout> seatLayouts =
                seatLayoutRepository.findByScreen(screen);

        List<ShowSeatInventory> inventory = seatLayouts
                .stream()
                .map(layout -> ShowSeatInventory.builder()
                        .show(show)
                        .seatLayout(layout)
                        .seatNumber(layout.getSeatNumber())
                        .status(SeatStatus.AVAILABLE)
                        .build())
                .collect(Collectors.toList());

        inventoryRepository.saveAll(inventory);

        log.info("Seat inventory generated for show {} totalSeats={}",
                show.getId(),
                inventory.size());

        return mapToResponse(show, pricing);
    }

    @Override
    public ShowResponseDto getShowById(Long showId) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        List<ShowSeatPricing> pricing =
                pricingRepository.findByShowId(showId);

        return mapToResponse(show, pricing);
    }

    @Override
    public List<ShowResponseDto> getShowsByMovie(Long movieId) {

        List<Show> shows = showRepository.findByMovieIdAndIsActiveTrue(movieId);

        return shows.stream()
                .map(this::mapShowOnly)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShowResponseDto> getShowsByScreen(Long screenId) {

        List<Show> shows =
                showRepository.findByScreenIdAndIsActiveTrue(screenId);

        return shows.stream()
                .map(this::mapShowOnly)
                .collect(Collectors.toList());
    }



    @Override
    @Transactional
    public void activateShow(Long showId) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        show.setIsActive(true);
        show.setUpdatedAt(LocalDateTime.now());

        log.info("Show {} activated", showId);
    }


    @Override
    @Transactional
    public void deactivateShow(Long showId) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        show.setIsActive(false);
        show.setUpdatedAt(LocalDateTime.now());

        log.info("Show {} deactivated", showId);
    }


    private ShowResponseDto mapToResponse(
            Show show,
            List<ShowSeatPricing> pricing) {

        List<ShowSeatPricingDto> pricingDtos = pricing.stream()
                .map(p -> {
                    ShowSeatPricingDto dto = new ShowSeatPricingDto();
                    dto.setTier(p.getTier());
                    dto.setPrice(p.getPrice());
                    return dto;
                })
                .collect(Collectors.toList());

        return ShowResponseDto.builder()
                .id(show.getId())
                .movieId(show.getMovie().getId())
                .movieTitle(show.getMovie().getTitle())
                .screenName(show.getScreen().getName())
                .screenSlug(show.getScreen().getSlug())
                .startTime(show.getStartTime())
                .endTime(show.getEndTime())
                .language(show.getLanguage())
                .format(show.getFormat())
                .isActive(show.getIsActive())
                .pricing(pricingDtos)
                .build();
    }


    private ShowResponseDto mapShowOnly(Show show) {

        return ShowResponseDto.builder()
                .id(show.getId())
                .movieId(show.getMovie().getId())
                .movieTitle(show.getMovie().getTitle())
                .screenName(show.getScreen().getName())
                .screenSlug(show.getScreen().getSlug())
                .startTime(show.getStartTime())
                .endTime(show.getEndTime())
                .language(show.getLanguage())
                .format(show.getFormat())
                .isActive(show.getIsActive())
                .build();
    }
}
