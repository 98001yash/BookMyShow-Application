package com.booking.BookMyShow.dtos;


import lombok.Data;

@Data
public class RegisterRequest {

    private String email;
    private String password;
}
