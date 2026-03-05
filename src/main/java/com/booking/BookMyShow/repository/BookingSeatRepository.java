package com.booking.BookMyShow.repository;

import com.booking.BookMyShow.entity.Booking;
import com.booking.BookMyShow.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingSeatRepository
        extends JpaRepository<BookingSeat, Long> {

    List<BookingSeat> findByBooking(Booking booking);
}