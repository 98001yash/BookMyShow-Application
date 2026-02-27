package com.booking.BookMyShow.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "movies",
        indexes = {
                @Index(name = "idx_movie_title", columnList = "title"),
                @Index(name = "idx_movie_active", columnList = "active")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    private String language;

    private String durationMinutes;

    private String genre;

    private LocalDate releaseDate;

    @Column(nullable = false)
    private Boolean active;
}
