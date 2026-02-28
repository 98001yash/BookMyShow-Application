package com.booking.BookMyShow.repository;

import com.booking.BookMyShow.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findBySlugAndActiveTrue(String slug);

    boolean existsByNameIgnoreCaseAndStateIgnoreCaseAndCountryIgnoreCase(
            String name,
            String state,
            String country
    );
}
