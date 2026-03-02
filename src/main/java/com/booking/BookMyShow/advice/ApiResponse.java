package com.booking.BookMyShow.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private Instant timestamp;
    private String traceId;
    private boolean success;
    private String message;
    private T data;
    private ApiError error;

    // --------------------------------------------------
    // SUCCESS RESPONSE
    // --------------------------------------------------
    public static <T> ApiResponse<T> success(T data, String traceId) {
        return ApiResponse.<T>builder()
                .timestamp(Instant.now())
                .traceId(traceId)
                .success(true)
                .message("Request processed successfully")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data, String traceId) {
        return ApiResponse.<T>builder()
                .timestamp(Instant.now())
                .traceId(traceId)
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    // --------------------------------------------------
    // FAILURE RESPONSE
    // --------------------------------------------------
    public static <T> ApiResponse<T> failure(ApiError error, String traceId) {
        return ApiResponse.<T>builder()
                .timestamp(Instant.now())
                .traceId(traceId)
                .success(false)
                .message(error.getMessage())
                .error(error)
                .build();
    }
}