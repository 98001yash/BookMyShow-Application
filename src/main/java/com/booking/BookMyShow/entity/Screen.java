package com.booking.BookMyShow.entity;

import com.booking.BookMyShow.enums.ScreenType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "screens",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_screen_name_per_theatre", columnNames = {"theatre_id", "name"}),
                @UniqueConstraint(name = "uk_screen_slug_per_theatre", columnNames = {"theatre_id", "slug"})
        },
        indexes = {
                @Index(name = "idx_screen_theatre", columnList = "theatre_id"),
                @Index(name = "idx_screen_active", columnList = "is_active")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Human readable name
    @Column(nullable = false)
    private String name;

    // Slug for routing
    @Column(nullable = false)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScreenType screenType;

    @Column(nullable = false)
    private Integer totalRows;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id", nullable = false)
    private Theatre theatre;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}