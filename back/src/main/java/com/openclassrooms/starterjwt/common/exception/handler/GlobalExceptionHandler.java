package com.openclassrooms.starterjwt.common.exception.handler;

import java.util.Map;
import java.util.HashMap;
import org.springframework.http.*;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.starterjwt.common.exception.NotFoundException;
import com.openclassrooms.starterjwt.common.error.response.ErrorResponse;
import com.openclassrooms.starterjwt.common.exception.BadRequestException;
import com.openclassrooms.starterjwt.common.exception.UnauthorizedException;
import com.openclassrooms.starterjwt.common.error.builder.ErrorResponseBuilder;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorResponseBuilder builder;
    private final Map<Class<? extends Exception>, HttpStatus> exceptionStatusMap = new HashMap<>();

    public GlobalExceptionHandler(ErrorResponseBuilder builder) {
        this.builder = builder;

        exceptionStatusMap.put(BadRequestException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(NotFoundException.class, HttpStatus.NOT_FOUND);
        exceptionStatusMap.put(UnauthorizedException.class, HttpStatus.FORBIDDEN);
        exceptionStatusMap.put(ExpiredJwtException.class, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAny(Exception ex, HttpServletRequest request) {
        HttpStatus status = exceptionStatusMap.getOrDefault(
            ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR
        );

        return ResponseEntity.status(status)
            .body(builder.build(status, ex.getMessage(), request));
    }
}