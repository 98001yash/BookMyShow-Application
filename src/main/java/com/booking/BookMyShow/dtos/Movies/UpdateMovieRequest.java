package com.booking.BookMyShow.dtos.Movies;


import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMovieRequest {

    @Size(max = 150)
    private String title;

    @Size(max = 50)
    private String language;
    private  String durationMinutes;

    @Size(max = 100)
    private String genre;
    private LocalDate releaseDate;
}
