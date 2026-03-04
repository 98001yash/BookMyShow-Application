package com.booking.BookMyShow.dtos.Show;


import com.booking.BookMyShow.enums.ShowFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ShowResponseDto {


    private Long id;

    private Long movieId;

    private String movieTitle;

    private String screenName;
    private String screenSlug;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String language;

    private ShowFormat format;

    private Boolean isActive;
    private List<ShowSeatPricingDto> pricing;
}
