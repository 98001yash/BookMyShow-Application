package com.booking.BookMyShow.dtos.Movies;


import com.booking.BookMyShow.enums.Certification;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMovieRequest {

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    @Size(max = 50)
    private String language;

    @NotBlank
    private String durationMinutes;

    @NotBlank
    @Size(max = 100)
    private String genre;

    @NotNull
    @PastOrPresent
    private LocalDate releaseDate;

    @Size(max = 2000)
    private String description;

    private String posterUrl;
    private String bannerImageUrl;
    private String trailerUrl;

    private Certification certification;

    private String imdbId;

    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private Double rating;

    private Boolean featured;
}