package com.booking.BookMyShow.dtos.Theatre;


import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TheatreResponseDto {


    private Long id;
    private String name;
    private Long cityId;
    private String cityName;

    private String contactNumber;

    private String slug;

    private Double latitude;
    private Double longitude;

    private String address;

    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
