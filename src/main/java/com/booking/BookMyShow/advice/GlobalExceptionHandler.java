package com.booking.BookMyShow.advice;

import com.booking.BookMyShow.exception.BaseException;
import com.booking.BookMyShow.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle custom exceptions
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<?>> handleBaseException(
            BaseException ex,
            HttpServletRequest request
    ) {

        log.warn("Business exception: {}", ex.getMessage());

        ApiError error = ApiError.builder()
                .status(ex.getHttpStatus().value())
                .errorCode(ex.getErrorCode().name())
                .message(ex.getMessage())
                .build();

        return buildResponse(error, ex.getHttpStatus());
    }

    // Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        List<String> subErrors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .collect(Collectors.toList());

        ApiError error = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errorCode(ErrorCode.VALIDATION_FAILED.name())
                .message("Validation failed")
                .subErrors(subErrors)
                .build();

        return buildResponse(error, HttpStatus.BAD_REQUEST);
    }

    // Catch-all (NEVER expose internal message)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {

        log.error("Unhandled exception", ex);

        ApiError error = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorCode(ErrorCode.INTERNAL_ERROR.name())
                .message("Something went wrong. Please try again later.")
                .build();

        return buildResponse(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiResponse<?>> buildResponse(ApiError error, HttpStatus status) {

        String traceId = MDC.get("traceId");

        return new ResponseEntity<>(
                ApiResponse.failure(error, traceId),
                status
        );
    }
}