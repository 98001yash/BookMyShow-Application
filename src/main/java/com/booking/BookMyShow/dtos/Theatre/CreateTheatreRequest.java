package com.booking.BookMyShow.dtos.Theatre;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTheatreRequest {

    @NotBlank
    private String name;

    @NotNull
    private String cityId;

    private String address;

    private String contactNumber;

    @NotNull
    private Double latitude;

    @NotNull
    private Double  longitude;
}
