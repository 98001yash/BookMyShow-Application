package com.booking.BookMyShow.repository;

import com.booking.BookMyShow.entity.Screen;
import com.booking.BookMyShow.entity.SeatLayout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatLayoutRepository extends JpaRepository<SeatLayout, Long> {

    List<SeatLayout> findByScreen(Screen screen);

    boolean existsByScreen(Screen screen);
}
