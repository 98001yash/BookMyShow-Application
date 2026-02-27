package com.booking.BookMyShow.advice;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApiError {

    private int status;
    private String errorCode;
    private String message;
    private List<String> subErrors;
}