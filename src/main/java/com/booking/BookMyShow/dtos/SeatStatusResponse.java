package com.booking.BookMyShow.dtos;


import com.booking.BookMyShow.enums.SeatStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatStatusResponse {

    private String seatNumber;
    private SeatStatus status;
}
