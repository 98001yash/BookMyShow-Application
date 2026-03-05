package com.booking.BookMyShow.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeaLockRequest {

    @NotEmpty
    private List<String> seatNumbers;
}
