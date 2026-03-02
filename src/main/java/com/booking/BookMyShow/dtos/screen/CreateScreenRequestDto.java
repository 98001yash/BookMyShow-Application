package com.booking.BookMyShow.dtos.screen;


import com.booking.BookMyShow.enums.ScreenType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateScreenRequestDto {

    private String name;
    private ScreenType screenType;
    private Integer totalRows;
    private Integer seatsPerRow;   // needed to calculate totalSeats
}
