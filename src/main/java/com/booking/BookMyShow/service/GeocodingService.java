package com.booking.BookMyShow.service;

import com.booking.BookMyShow.dtos.GeoPoint;

public interface GeocodingService {

    GeoPoint geocode(String address);
}