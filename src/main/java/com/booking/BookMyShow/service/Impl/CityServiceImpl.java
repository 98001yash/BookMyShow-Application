package com.booking.BookMyShow.service.Impl;

import com.booking.BookMyShow.dtos.city.CityResponseDto;
import com.booking.BookMyShow.dtos.city.CreateCityRequest;
import com.booking.BookMyShow.dtos.city.PublicCityResponse;
import com.booking.BookMyShow.entity.City;
import com.booking.BookMyShow.exception.ResourceAlreadyExistsException;
import com.booking.BookMyShow.exception.ResourceNotFoundException;
import com.booking.BookMyShow.repository.CityRepository;
import com.booking.BookMyShow.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public CityResponseDto createCity(CreateCityRequest request) {

        log.info("Admin creating city: {} - {} - {}",
                request.getName(),
                request.getState(),
                request.getCountry());

        boolean exists = cityRepository.existsByNameIgnoreCaseAndStateIgnoreCaseAndCountryIgnoreCase(
                request.getName(),
                request.getState(),
                request.getCountry()
        );

        if(exists){
            throw new ResourceAlreadyExistsException("City already exists with the same name, state and country");
        }
        String slug =generateSlug(request.getName());

        City city = City.builder()
                .name(request.getName().trim())
                .state(request.getState().trim())
                .country(request.getCountry().trim())
                .slug(slug)
                .active(true)
                .build();

        City saved = cityRepository.save(city);
        log.info("City created successfully with ID: {}",saved.getId());
        return mapToResponse(saved);
    }

    @Override
    public CityResponseDto getCityById(Long id) {


        City city = cityRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("City not found"));
        return mapToResponse(city);
    }

    @Override
    public Page<CityResponseDto> getAllCities(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        List<String> allowedSortFields = List.of(
                "id",
                "name",
                "state",
                "country",
                "createdAt"
        );

        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "id";
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<City> cityPage = cityRepository.findAll(pageable);

        return cityPage.map(this::mapToResponse);
    }

    @Override
    public void activateCity(Long id) {

        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));

        city.setActive(true);
        cityRepository.save(city);

        log.info("City activated: {}", id);
    }

    @Override
    public void deactivateCity(Long id) {

        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));

        city.setActive(false);
        cityRepository.save(city);

        log.info("City deactivated: {}", id);
    }

    @Override
    public List<PublicCityResponse> getActiveCities() {

        log.info("Fetching all active cities for public API");

        return cityRepository.findAllByActiveTrueOrderByNameAsc()
                .stream()
                .map(city -> PublicCityResponse.builder()
                        .id(city.getId())
                        .name(city.getName())
                        .slug(city.getSlug())
                        .build()
                )
                .toList();
    }

    @Override
    public PublicCityResponse getCityBySlug(String slug) {
        City city = cityRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));

        return PublicCityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .slug(city.getSlug())
                .build();
    }

    private CityResponseDto mapToResponse(City city) {

        return CityResponseDto.builder()
                .id(city.getId())
                .name(city.getName())
                .state(city.getState())
                .country(city.getCountry())
                .slug(city.getSlug())
                .active(city.getActive())
                .createdAt(city.getCreatedAt())
                .build();
    }

    // ----------------------------
    // SLUG GENERATOR
    // ----------------------------
    private String generateSlug(String name) {

        return name.trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");
    }
}
