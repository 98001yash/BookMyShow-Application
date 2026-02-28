package com.booking.BookMyShow.dtos.city;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicCityResponse {

    private Long id;
    private String name;
    private String slug;
}
