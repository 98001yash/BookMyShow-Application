package com.booking.BookMyShow.entity;

import com.booking.BookMyShow.enums.SeatTier;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "show_seat_pricing",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_show_tier",
                        columnNames = {"show_id", "tier"}
                )
        },
        indexes = {
                @Index(name = "idx_pricing_show", columnList = "show_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowSeatPricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatTier tier;   // SILVER / GOLD / PLATINUM

    @Column(nullable = false)
    private BigDecimal price;
}