package com.booking.BookMyShow.dtos.city;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCityRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String state;

    @NotBlank
    private String country;
}
