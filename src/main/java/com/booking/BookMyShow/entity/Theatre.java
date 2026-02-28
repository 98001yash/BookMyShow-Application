package com.booking.BookMyShow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "theatres",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_theatre_name_city",
                        columnNames = {"name", "city_id"}
                )
        },
        indexes = {
                @Index(name = "idx_theatre_city_id", columnList = "city_id"),
                @Index(name = "idx_theatre_active", columnList = "active"),
                @Index(name = "idx_theatre_slug", columnList = "slug")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(length = 300)
    private String address;

    @Column(length = 15)
    private String contactNumber;

    @Column(nullable = false, unique = true, length = 200)
    private String slug;

    private Double latitude;

    private Double longitude;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}