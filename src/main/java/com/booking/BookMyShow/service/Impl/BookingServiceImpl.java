package com.booking.BookMyShow.service.Impl;

import com.booking.BookMyShow.dtos.Booking.BookingResponse;
import com.booking.BookMyShow.dtos.Booking.CreateBookingRequest;
import com.booking.BookMyShow.entity.*;
import com.booking.BookMyShow.enums.BookingStatus;
import com.booking.BookMyShow.enums.SeatStatus;
import com.booking.BookMyShow.enums.SeatTier;
import com.booking.BookMyShow.exception.ResourceNotFoundException;
import com.booking.BookMyShow.repository.*;
import com.booking.BookMyShow.service.BookingService;
import com.booking.BookMyShow.utils.BookingReferenceGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final ShowSeatInventoryRepository inventoryRepository;
    private final ShowSeatPricingRepository pricingRepository;
    private final ShowRepository showRepository;

    @Override
    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request) {

        log.info("Creating booking for showId={} seats={} idempotencyKey={}",
                request.getShowId(),
                request.getSeatNumbers(),
                request.getIdempotencyKey());

        // 1️ Idempotency protection
        bookingRepository.findByIdempotencyKey(request.getIdempotencyKey())
                .ifPresent(existing -> {
                    throw new RuntimeException("Duplicate booking request with same idempotency key");
                });

        // 2️ Fetch show
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        List<String> seatNumbers = request.getSeatNumbers();

        List<ShowSeatInventory> lockedSeats = new ArrayList<>();

        BigDecimal totalAmount = BigDecimal.ZERO;

        // 3️ Validate seats + calculate pricing
        for (String seatNumber : seatNumbers) {

            ShowSeatInventory seat = inventoryRepository
                    .findWithLockByShowIdAndSeatLayout_SeatNumber(
                            request.getShowId(),
                            seatNumber
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Seat not found: " + seatNumber));

            if (seat.getStatus() != SeatStatus.LOCKED) {
                throw new RuntimeException("Seat not locked: " + seatNumber);
            }

            lockedSeats.add(seat);

            SeatTier tier = seat.getSeatLayout().getTier();

            ShowSeatPricing pricing = pricingRepository
                    .findByShowAndTier(show, tier)
                    .orElseThrow(() ->
                            new RuntimeException("Pricing not found for tier " + tier));

            totalAmount = totalAmount.add(pricing.getPrice());
        }

        // 4️Generate booking reference
        String bookingReference = BookingReferenceGenerator.generate();

        // ⃣ Create booking
        Booking booking = Booking.builder()
                .bookingReference(bookingReference)
                .idempotencyKey(request.getIdempotencyKey())
                .show(show)
                .userId(request.getUserId())
                .status(BookingStatus.SEATS_LOCKED)
                .totalAmount(totalAmount.doubleValue())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        bookingRepository.save(booking);

        log.info("Booking created with reference={}", bookingReference);

        // 6️ Save booking seats
        List<BookingSeat> bookingSeats = new ArrayList<>();

        for (ShowSeatInventory seat : lockedSeats) {

            SeatTier tier = seat.getSeatLayout().getTier();

            ShowSeatPricing pricing = pricingRepository
                    .findByShowAndTier(show, tier)
                    .orElseThrow(() ->
                            new RuntimeException("Pricing not found for tier " + tier));

            BookingSeat bookingSeat = BookingSeat.builder()
                    .booking(booking)
                    .showSeatInventory(seat)
                    .price(pricing.getPrice().doubleValue())
                    .build();

            bookingSeats.add(bookingSeat);
        }

        bookingSeatRepository.saveAll(bookingSeats);

        // 7️ Move booking to payment stage
        booking.setStatus(BookingStatus.PAYMENT_PENDING);
        booking.setUpdatedAt(LocalDateTime.now());

        bookingRepository.save(booking);

        log.info("Booking ready for payment reference={} totalAmount={}",
                bookingReference,
                totalAmount);

        // 8️ Response
        return BookingResponse.builder()
                .bookingId(booking.getId())
                .bookingReference(bookingReference)
                .showId(show.getId())
                .seats(seatNumbers)
                .totalAmount(totalAmount.doubleValue())
                .status(booking.getStatus().name())
                .build();
    }

    @Override
    public BookingResponse getBooking(String reference) {

        Booking booking = bookingRepository
                .findByBookingReference(reference)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        List<BookingSeat> seats =
                bookingSeatRepository.findByBooking(booking);

        List<String> seatNumbers = seats
                .stream()
                .map(s -> s.getShowSeatInventory().getSeatNumber())
                .toList();

        return BookingResponse.builder()
                .bookingId(booking.getId())
                .bookingReference(reference)
                .showId(booking.getShow().getId())
                .seats(seatNumbers)
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus().name())
                .build();
    }
}