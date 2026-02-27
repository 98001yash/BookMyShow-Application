package com.booking.BookMyShow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booking_seats",
        indexes = {
                @Index(name = "idx_booking_seat_booking", columnList = "booking_id"),
                @Index(name = "idx_booking_seat_show_seat",
                        columnList = "showSeatInventory_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showSeatInventory_id", nullable = false)
    private ShowSeatInventory showSeatInventory;
}