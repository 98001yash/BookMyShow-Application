package com.booking.BookMyShow.dtos.Movies;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponseDto {


    private Long id;
    private String title;
    private String language;
    private String durationMinutes;
    private String genre;
    private LocalDate releaseDate;
    private boolean active;
}
