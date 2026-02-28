package com.booking.BookMyShow.dtos.Movies;


import com.booking.BookMyShow.enums.Certification;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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

    private String durationMinutes;

    @Size(max = 100)
    private String genre;

    private LocalDate releaseDate;

    private String description;

    private String posterUrl;

    private String bannerImageUrl;

    private String trailerUrl;

    private Certification certification;

    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private Double rating;

    private Integer popularityScore;

    private Boolean featured;

    private Boolean active;
}