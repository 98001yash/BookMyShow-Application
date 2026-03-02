package com.booking.BookMyShow.service.Impl;

import com.booking.BookMyShow.dtos.GeoPoint;
import com.booking.BookMyShow.service.GeocodingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OSMGeocodingService implements GeocodingService {

    private final RestTemplate restTemplate;

    public OSMGeocodingService() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.setRequestFactory(clientHttpRequestFactory());
    }

    @Override
    public GeoPoint geocode(String address) {

        try {

            String url = UriComponentsBuilder
                    .fromUriString("https://nominatim.openstreetmap.org/search")
                    .queryParam("q", address)
                    .queryParam("format", "json")
                    .queryParam("limit", 1)
                    .build()
                    .encode()
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "BookMyShow-App (contact@example.com)");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<List<Map<String, Object>>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
                    );

            List<Map<String, Object>> body = response.getBody();

            if (body == null || body.isEmpty()) {
                log.warn("No geocode result found for address: {}", address);
                return null;
            }

            Map<String, Object> result = body.get(0);

            Double lat = Double.parseDouble(result.get("lat").toString());
            Double lon = Double.parseDouble(result.get("lon").toString());

            return new GeoPoint(lat, lon);

        } catch (RestClientException e) {
            log.error("OSM API call failed for address: {}", address, e);
            return null;

        } catch (Exception e) {
            log.error("Unexpected geocoding error for address: {}", address, e);
            return null;
        }
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);

        return factory;
    }
}