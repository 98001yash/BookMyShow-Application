package com.booking.BookMyShow.controller.Theatre;

import com.booking.BookMyShow.advice.ApiResponse;
import com.booking.BookMyShow.dtos.Theatre.PublicTheatreResponse;
import com.booking.BookMyShow.dtos.Theatre.TheatreSummaryResponse;
import com.booking.BookMyShow.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/theatres")
@RequiredArgsConstructor
public class PublicTheatreController {

    private final TheatreService theatreService;


    @GetMapping
    public ApiResponse<Page<TheatreSummaryResponse>> getTheatresByCity(
            @RequestParam Long cityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        return ApiResponse.success(
                theatreService.getTheatresByCity(
                        cityId,
                        page,
                        size,
                        sortBy,
                        direction
                ),
                response);
    }


    @GetMapping("/nearby")
    public ApiResponse<List<PublicTheatreResponse>> getNearbyTheatres(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "10") double radius
    ) {

        return ApiResponse.success(
                theatreService.getNearbyTheatres(
                        lat,
                        lng,
                        radius
                ),
                response);
    }
}
