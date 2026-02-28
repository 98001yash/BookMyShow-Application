package com.booking.BookMyShow.entity;

import com.booking.BookMyShow.enums.Certification;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "movies",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_movie_title_language",
                        columnNames = {"title", "language"}
                ),
                @UniqueConstraint(
                        name = "uk_movie_imdb",
                        columnNames = {"imdbId"}
                )
        },
        indexes = {
                @Index(name = "idx_movie_title", columnList = "title"),
                @Index(name = "idx_movie_active", columnList = "active"),
                @Index(name = "idx_movie_language", columnList = "language"),
                @Index(name = "idx_movie_release", columnList = "releaseDate"),
                @Index(name = "idx_movie_rating", columnList = "rating"),
                @Index(name = "idx_movie_popularity", columnList = "popularityScore")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Info
    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 50)
    private String language;

    @Column(nullable = false)
    private String durationMinutes;

    @Column(nullable = false, length = 100)
    private String genre;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Column(nullable = false)
    private Boolean active;

    // Metadata
    @Column(length = 2000)
    private String description;

    private String posterUrl;

    private String bannerImageUrl;

    private String trailerUrl;

    @Enumerated(EnumType.STRING)
    private Certification certification;

    @Column(unique = true)
    private String imdbId;

    private Double rating; // 0–10 scale

    @Column(nullable = false)
    private Integer popularityScore;

    @Column(nullable = false)
    private Boolean featured;

    // Audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.popularityScore == null) this.popularityScore = 0;
        if (this.featured == null) this.featured = false;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}