package com.booking.BookMyShow.dtos.Show;


import com.booking.BookMyShow.enums.SeatTier;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShowSeatPricingDto {


    @NotNull(message = "Seat tier is required")
    private SeatTier tier;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
}
