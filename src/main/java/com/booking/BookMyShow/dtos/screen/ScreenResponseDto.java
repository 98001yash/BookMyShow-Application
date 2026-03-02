package com.booking.BookMyShow.dtos.screen;


import com.booking.BookMyShow.enums.ScreenType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScreenResponseDto {

    private String name;
    private String slug;
    private ScreenType screenType;
    private Integer totalRows;
    private Integer totalSeats;
    private Boolean isActive;
}
