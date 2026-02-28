package com.booking.BookMyShow.dtos.Theatre;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicTheatreResponse {


    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;

    private Double distance; // used only in geo-search

}
