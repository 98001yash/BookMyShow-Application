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
    private T data;
    private ApiError error;

    public static <T> ApiResponse<T> success(T data, String traceId) {
        return ApiResponse.<T>builder()
                .timestamp(Instant.now())
                .traceId(traceId)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> failure(ApiError error, String traceId) {
        return ApiResponse.<T>builder()
                .timestamp(Instant.now())
                .traceId(traceId)
                .error(error)
                .build();
    }
}