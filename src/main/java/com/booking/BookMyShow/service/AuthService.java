package com.booking.BookMyShow.service;

import com.booking.BookMyShow.dtos.AuthResponse;
import com.booking.BookMyShow.dtos.LoginRequest;
import com.booking.BookMyShow.dtos.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
