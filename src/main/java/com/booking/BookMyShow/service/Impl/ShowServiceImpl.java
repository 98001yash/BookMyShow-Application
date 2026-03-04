package com.booking.BookMyShow.service.Impl;

import com.booking.BookMyShow.dtos.Show.CreateShowRequestDto;
import com.booking.BookMyShow.dtos.Show.ShowResponseDto;
import com.booking.BookMyShow.dtos.Show.ShowSeatPricingDto;
import com.booking.BookMyShow.entity.Show;
import com.booking.BookMyShow.entity.ShowSeatPricing;
import com.booking.BookMyShow.repository.*;
import com.booking.BookMyShow.service.ShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public ShowResponseDto createShow(CreateShowRequestDto request) {
        return null;
    }

    @Override
    public ShowResponseDto getShowById(Long showId) {
        return null;
    }

    @Override
    public List<ShowResponseDto> getShowsByMovie(Long movieId) {
        return List.of();
    }

    @Override
    public List<ShowResponseDto> getShowsByScreen(Long screenId) {
        return List.of();
    }

    @Override
    public void activateShow(Long showId) {

    }

    @Override
    public void deactivateShow(Long showId) {

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
}
