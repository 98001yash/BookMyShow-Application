package com.booking.BookMyShow.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "screens",
        indexes = {
                @Index(name = "idx_screen_theatre", columnList = "theatre_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer totalSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id", nullable = false)
    private Theatre theatre;
}
