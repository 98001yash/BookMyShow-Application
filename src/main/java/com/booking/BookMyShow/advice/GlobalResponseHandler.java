package com.booking.BookMyShow.advice;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        // 🔹 Skip Swagger/OpenAPI endpoints
        if (request.getURI().getPath().contains("/v3/api-docs") ||
                request.getURI().getPath().contains("/swagger")) {
            return body;
        }

        // 🔹 If already wrapped, don't wrap again
        if (body instanceof ApiResponse<?>) {
            return body;
        }

        String traceId = MDC.get("traceId");

        // 🔹 Handle ResponseEntity separately
        if (body instanceof ResponseEntity<?> responseEntity) {

            Object responseBody = responseEntity.getBody();

            if (responseBody instanceof ApiResponse<?>) {
                return body;
            }

            ApiResponse<Object> wrapped =
                    ApiResponse.success(responseBody, traceId);

            return ResponseEntity
                    .status(responseEntity.getStatusCode())
                    .headers(responseEntity.getHeaders())
                    .body(wrapped);
        }

        // 🔹 Handle null body
        if (body == null) {
            return ApiResponse.success(null, traceId);
        }

        // 🔹 Handle String response properly (important!)
        if (body instanceof String) {
            return ApiResponse.success(body, traceId).toString();
        }

        // 🔹 Default wrapping
        return ApiResponse.success(body, traceId);
    }
}