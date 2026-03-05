package com.booking.BookMyShow.dtos;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatLockResponse {

    private Long showId;
    private List<String> lockedSeats;
    private LocalDateTime lockedUntil;
}
