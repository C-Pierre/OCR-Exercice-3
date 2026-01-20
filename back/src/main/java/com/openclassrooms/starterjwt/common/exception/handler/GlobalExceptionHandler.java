package com.openclassrooms.starterjwt.common.exception.handler;

import org.springframework.http.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.starterjwt.common.error.response.ErrorResponse;
import com.openclassrooms.starterjwt.common.error.builder.ErrorResponseBuilder;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorResponseBuilder builder;

    public GlobalExceptionHandler(ErrorResponseBuilder builder) {
        this.builder = builder;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAny(Exception ex, HttpServletRequest request) {
        HttpStatus status = resolveHttpStatus(ex);

        return ResponseEntity.status(status)
            .body(builder.build(status, ex.getMessage(), request));
    }

    private HttpStatus resolveHttpStatus(Exception ex) {
        ResponseStatus responseStatus =
            ex.getClass().getAnnotation(ResponseStatus.class);

        if (responseStatus != null) {
            return responseStatus.value();
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}