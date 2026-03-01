package com.booking.BookMyShow.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeoPoint {
    private Double latitude;
    private Double longitude;
}