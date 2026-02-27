package com.booking.BookMyShow.dtos.Movies;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMovieRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 150)
    private String title;

    @NotBlank(message = "language is required")
    @Size(max = 50)
    private String language;

    @NotBlank(message = "duration is required")
    private String durationMinutes;

    @NotBlank(message = "Genre is required")
    @Size(max = 100)
    private String genre;

    @NotNull(message = "Release date is required")
    @PastOrPresent(message = "Release date cannot be in the future")
    private LocalDate releaseDate;


}
