package com.booking.BookMyShow.service.Impl;

import com.booking.BookMyShow.dtos.GeoPoint;
import com.booking.BookMyShow.dtos.Theatre.*;
import com.booking.BookMyShow.entity.City;
import com.booking.BookMyShow.entity.Theatre;
import com.booking.BookMyShow.exception.ResourceAlreadyExistsException;
import com.booking.BookMyShow.exception.ResourceNotFoundException;
import com.booking.BookMyShow.repository.CityRepository;
import com.booking.BookMyShow.repository.TheatreRepository;
import com.booking.BookMyShow.service.GeocodingService;
import com.booking.BookMyShow.service.TheatreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;
    private final CityRepository cityRepository;
    private final GeocodingService geocodingService;

    @Override
    @Transactional
    public TheatreResponseDto createTheatre(CreateTheatreRequest request) {

        log.info("Creating theatre: {} in cityId: {}", request.getName(), request.getCityId());

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));

        boolean exists = theatreRepository
                .existsByNameIgnoreCaseAndCity_Id(
                        request.getName(),
                        request.getCityId()
                );

        if (exists) {
            throw new ResourceAlreadyExistsException(
                    "Theatre already exists in this city"
            );
        }

        String slug = generateSlug(request.getName(), city.getName());

        // 🔥 AUTO GEOCODING
        GeoPoint geoPoint = geocodingService.geocode(
                request.getAddress() + ", " + city.getName()
        );

        Double latitude = null;
        Double longitude = null;

        if (geoPoint != null) {
            latitude = geoPoint.getLatitude();
            longitude = geoPoint.getLongitude();
        } else {
            log.warn("Geocoding failed for theatre: {}. Saving without coordinates.",
                    request.getName());
        }

        Theatre theatre = Theatre.builder()
                .name(request.getName())
                .city(city)
                .address(request.getAddress())
                .contactNumber(request.getContactNumber())
                .slug(slug)
                .latitude(latitude)
                .longitude(longitude)
                .active(true)
                .build();

        Theatre saved = theatreRepository.save(theatre);

        return mapToResponse(saved);
    }


    @Override
    @Transactional
    public TheatreResponseDto updateTheatre(Long theatreId, UpdateTheatreRequest request) {

        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));

        if (request.getName() != null) {
            theatre.setName(request.getName());
        }

        if (request.getAddress() != null) {
            theatre.setAddress(request.getAddress());

            GeoPoint geoPoint = geocodingService.geocode(
                    request.getAddress() + ", " + theatre.getCity().getName()
            );

            if (geoPoint != null) {
                theatre.setLatitude(geoPoint.getLatitude());
                theatre.setLongitude(geoPoint.getLongitude());
            } else {
                log.warn("Geocoding failed during update for theatreId: {}", theatreId);
            }
        }

        if (request.getContactNumber() != null) {
            theatre.setContactNumber(request.getContactNumber());
        }

        if (request.getActive() != null) {
            theatre.setActive(request.getActive());
        }

        Theatre updated = theatreRepository.save(theatre);

        return mapToResponse(updated);
    }

    @Override
    public void activateTheatre(Long theatreId) {
        toggleStatus(theatreId, true);
    }

    @Override
    public void deactivateTheatre(Long theatreId) {
        toggleStatus(theatreId, false);
    }


    private void toggleStatus(Long theatreId, boolean status) {

        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));

        theatre.setActive(status);
        theatreRepository.save(theatre);
    }

    @Override
    @Transactional(readOnly = true)
    public TheatreResponseDto getTheatreBySlug(String slug) {
        Theatre theatre = theatreRepository
                .findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));

        return mapToResponse(theatre);
    }

    @Override
    public Page<TheatreSummaryResponse> getTheatresByCity(Long cityId,
                                                          int page,
                                                          int size,
                                                          String sortBy,
                                                          String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return theatreRepository
                .findAllByCity_IdAndActiveTrue(cityId, pageable)
                .map(this::mapToSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicTheatreResponse> getNearbyTheatres(double latitude,
                                                          double longitude,
                                                          double radius) {
        List<Theatre> theatres =
                theatreRepository.findNearbyTheatres(latitude, longitude, radius);

        return theatres.stream()
                .map(theatre -> PublicTheatreResponse.builder()
                        .id(theatre.getId())
                        .name(theatre.getName())
                        .address(theatre.getAddress())
                        .latitude(theatre.getLatitude())
                        .longitude(theatre.getLongitude())
                        .distance(null) // can calculate separately if needed
                        .build())
                .collect(Collectors.toList());
    }

    // MAPPERS

    private TheatreResponseDto mapToResponse(Theatre theatre) {

        return TheatreResponseDto.builder()
                .id(theatre.getId())
                .name(theatre.getName())
                .cityId(theatre.getCity().getId())
                .cityName(theatre.getCity().getName())
                .address(theatre.getAddress())
                .contactNumber(theatre.getContactNumber())
                .slug(theatre.getSlug())
                .latitude(theatre.getLatitude())
                .longitude(theatre.getLongitude())
                .active(theatre.getActive())
                .createdAt(theatre.getCreatedAt())
                .updatedAt(theatre.getUpdatedAt())
                .build();
    }

    private TheatreSummaryResponse mapToSummary(Theatre theatre) {

        return TheatreSummaryResponse.builder()
                .id(theatre.getId())
                .name(theatre.getName())
                .cityName(theatre.getCity().getName())
                .slug(theatre.getSlug())
                .build();
    }


    // SLUG GENERATOR
    private String generateSlug(String theatreName, String cityName) {

        String base = theatreName + "-" + cityName;

        String normalized = Normalizer.normalize(base, Normalizer.Form.NFD)
                .replaceAll("[^\\w\\s-]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase(Locale.ENGLISH);

        return normalized + "-" + UUID.randomUUID().toString().substring(0, 6);
    }
}
