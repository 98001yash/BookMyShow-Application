package com.booking.BookMyShow.entity;

import com.booking.BookMyShow.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "show_seat_inventory",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_show_seat",
                        columnNames = {"show_id", "seat_number"}
                )
        },
        indexes = {
                @Index(name = "idx_inventory_show", columnList = "show_id"),
                @Index(name = "idx_inventory_status", columnList = "status"),
                @Index(name = "idx_inventory_show_status",
                        columnList = "show_id, status"),
                @Index(name = "idx_inventory_locked_until",
                        columnList = "locked_until"),
                @Index(name = "idx_show_seat_layout",
                        columnList = "show_id, seat_layout_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowSeatInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_layout_id", nullable = false)
    private SeatLayout seatLayout;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;
}
