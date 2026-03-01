package com.booking.BookMyShow.service.Impl;

import com.booking.BookMyShow.dtos.GeoPoint;
import com.booking.BookMyShow.service.GeocodingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OSMGeocodingService implements GeocodingService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public GeoPoint geocode(String address) {

        try {

            String url = "https://nominatim.openstreetmap.org/search?q="
                    + address.replace(" ", "+")
                    + "&format=json&limit=1";

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "BookMyShow-App");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<Map<String, Object>>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<>() {}
                    );

            List<Map<String, Object>> body = response.getBody();

            if (body == null || body.isEmpty()) {
                throw new RuntimeException("Unable to geocode address");
            }

            Map<String, Object> result = body.get(0);

            Double lat = Double.parseDouble(result.get("lat").toString());
            Double lon = Double.parseDouble(result.get("lon").toString());

            return new GeoPoint(lat, lon);

        } catch (Exception e) {
            log.error("Geocoding failed for address: {}", address, e);
            throw new RuntimeException("Failed to geocode address");
        }
    }
}