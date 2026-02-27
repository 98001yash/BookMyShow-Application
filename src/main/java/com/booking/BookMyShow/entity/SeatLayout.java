package com.booking.BookMyShow.entity;


import com.booking.BookMyShow.enums.SeatTier;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seat_layout",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_screen_seat",
                        columnNames = {"screen_id", "seat_number"})
        },
        indexes = {
                @Index(name = "idx_seat_layout_screen", columnList = "screen_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatLayout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private String rowLabel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatTier tier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;
}
