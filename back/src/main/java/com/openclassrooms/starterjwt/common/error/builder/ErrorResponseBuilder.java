package com.openclassrooms.starterjwt.common.error.builder;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import com.openclassrooms.starterjwt.common.error.response.ErrorResponse;

@Component
public class ErrorResponseBuilder {

    public ErrorResponse build(HttpStatus status, String message, HttpServletRequest request) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
    }
}