package com.openclassrooms.starterjwt.common.exception;

import com.openclassrooms.starterjwt.common.error.builder.ErrorResponseBuilder;
import com.openclassrooms.starterjwt.common.error.response.ErrorResponse;
import com.openclassrooms.starterjwt.common.exception.BadRequestException;
import com.openclassrooms.starterjwt.common.exception.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerBadRequestTest {

    private ErrorResponseBuilder builder;
    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        builder = mock(ErrorResponseBuilder.class);
        handler = new GlobalExceptionHandler(builder);
        request = mock(HttpServletRequest.class);
    }

    @Test
    void handleAny_shouldReturnBadRequest_forBadRequestException() {
        BadRequestException ex = new BadRequestException("Invalid input");
        ErrorResponse fakeResponse = mock(ErrorResponse.class);

        when(builder.build(any(), anyString(), any()))
                .thenReturn(fakeResponse);

        var responseEntity = handler.handleAny(ex, request);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo(fakeResponse);
    }

    @Test
    void handleExpiredJwtException_shouldReturnUnauthorized() {
        ExpiredJwtException ex = new ExpiredJwtException("Token expired");
        ErrorResponse fakeResponse = mock(ErrorResponse.class);

        when(builder.build(any(), anyString(), any()))
                .thenReturn(fakeResponse);

        var responseEntity = handler.handleAny(ex, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isEqualTo(fakeResponse);
    }

    @Test
    void handleNotFoundException_shouldReturnNotFound() {
        NotFoundException ex = new NotFoundException("Resource not found");
        ErrorResponse fakeResponse = mock(ErrorResponse.class);

        when(builder.build(any(), anyString(), any()))
                .thenReturn(fakeResponse);

        var responseEntity = handler.handleAny(ex, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo(fakeResponse);
    }

    @Test
    void handleUnauthorizedException_shouldReturnUnauthorized() {
        UnauthorizedException ex = new UnauthorizedException("Unauthorized access");
        ErrorResponse fakeResponse = mock(ErrorResponse.class);

        when(builder.build(any(), anyString(), any()))
                .thenReturn(fakeResponse);

        var responseEntity = handler.handleAny(ex, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isEqualTo(fakeResponse);
    }
}
