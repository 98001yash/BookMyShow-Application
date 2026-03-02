package com.booking.BookMyShow.service.Impl;

import com.booking.BookMyShow.dtos.GeoPoint;
import com.booking.BookMyShow.service.GeocodingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenCageGeocodingService implements GeocodingService {

    @Value("${geocoding.opencage.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public GeoPoint geocode(String address) {

        try {

            String url = UriComponentsBuilder
                    .fromUriString("https://api.opencagedata.com/geocode/v1/json")
                    .queryParam("q", address)
                    .queryParam("key", apiKey)
                    .queryParam("limit", 1)
                    .build()
                    .encode()
                    .toUriString();

            ResponseEntity<Map<String, Object>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<Map<String, Object>>() {}
                    );

            Map<String, Object> body = response.getBody();

            if (body == null || body.get("results") == null) {
                log.warn("No geocode result found for address: {}", address);
                return null;
            }

            List<Map<String, Object>> results =
                    (List<Map<String, Object>>) body.get("results");

            if (results.isEmpty()) {
                log.warn("No geocode results returned for address: {}", address);
                return null;
            }

            Map<String, Object> geometry =
                    (Map<String, Object>) results.get(0).get("geometry");

            Double lat = Double.valueOf(geometry.get("lat").toString());
            Double lng = Double.valueOf(geometry.get("lng").toString());

            return new GeoPoint(lat, lng);

        } catch (RestClientException e) {

            log.error("OpenCage API call failed for address: {}", address, e);
            return null;

        } catch (Exception e) {

            log.error("Unexpected geocoding error for address: {}", address, e);
            return null;
        }
    }
}
