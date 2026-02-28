package com.booking.BookMyShow.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private Instant timestamp;
    private String traceId;
    private T data;
    private ApiError error;

    // -----------------------------
    // SUCCESS RESPONSE
    // -----------------------------
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .timestamp(Instant.now())
                .traceId(generateTraceId())
                .data(data)
                .build();
    }

    // -----------------------------
    // FAILURE RESPONSE
    // -----------------------------
    public static <T> ApiResponse<T> failure(ApiError error, String traceId) {
        return ApiResponse.<T>builder()
                .timestamp(Instant.now())
                .traceId(generateTraceId())
                .error(error)
                .build();
    }

    // -----------------------------
    // TRACE ID GENERATOR
    // -----------------------------
    private static String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}