package com.booking.BookMyShow.dtos.Show;


import com.booking.BookMyShow.enums.ShowFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateShowRequestDto {

    @NotNull(message = "Movie ID is required")
    private Long movieId;

    @NotBlank(message = "Screen slug is required")
    private String screenSlug;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @NotBlank(message = "Language is required")
    private String language;

    @NotNull(message = "Show format is required")
    private ShowFormat format;

    @NotEmpty(message = "Seat pricing must be provided")
    @Valid
    private List<ShowSeatPricingDto> pricing;
}
