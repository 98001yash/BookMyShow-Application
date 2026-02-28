package com.booking.BookMyShow.dtos.Movies;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieSummaryResponse {


    private Long id;

    private String title;

    private String language;

    private Boolean active;
}
