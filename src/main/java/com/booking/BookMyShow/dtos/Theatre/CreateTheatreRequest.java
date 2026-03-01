package com.booking.BookMyShow.dtos.Theatre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTheatreRequest {

    @NotBlank
    private String name;

    @NotNull
    private Long cityId;

    @NotBlank
    private String address;

    private String contactNumber;
}