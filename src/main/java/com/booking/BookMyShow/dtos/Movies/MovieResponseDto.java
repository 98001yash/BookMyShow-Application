package com.booking.BookMyShow.dtos.Movies;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieResponseDto {


    private Long id;
    private String title;
    private String language;
    private String durationMinutes;
    private String genre;
    private LocalDate releaseDate;
    private boolean active;
}
