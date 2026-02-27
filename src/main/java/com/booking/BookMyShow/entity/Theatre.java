package com.booking.BookMyShow.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "theatres",
        indexes = {
                @Index(name = "idx_theatre_city", columnList = "city"),
                @Index(name = "idx_theatre_active", columnList = "active")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theatre {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    private String address;

    @Column(nullable = false)
    private boolean active;
}
