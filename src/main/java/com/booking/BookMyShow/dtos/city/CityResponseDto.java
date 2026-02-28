package com.booking.BookMyShow.dtos.city;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityResponseDto {

    private Long id;
    private String name;
    private String state;
    private String country;
    private String slug;
    private Boolean active;
    private LocalDateTime createdAt;
}