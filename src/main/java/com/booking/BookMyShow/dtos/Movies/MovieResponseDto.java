package com.booking.BookMyShow.dtos.Movies;


import com.booking.BookMyShow.enums.Certification;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private String description;
    private String posterUrl;
    private String bannerImageUrl;
    private String trailerUrl;

    private Certification certification;
    private String imdbId;
    private Double rating;
    private Integer popularityScore;
    private Boolean featured;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}