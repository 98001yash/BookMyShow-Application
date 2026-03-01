package com.booking.BookMyShow.dtos.Theatre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TheatreSummaryResponse {

    private Long id;

    private String name;

    private String cityName;

    private String slug;

    private String address;

    private Double latitude;

    private Double longitude;

}

