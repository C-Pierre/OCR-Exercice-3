package com.openclassrooms.starterjwt.common.exception.handler;

import com.openclassrooms.starterjwt.common.error.builder.ErrorResponseBuilder;
import com.openclassrooms.starterjwt.common.error.response.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ResponseStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

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
    void handleAny_shouldReturnInternalServerError_forGenericException() {
        Exception ex = new Exception("Something went wrong");
        ErrorResponse fakeResponse = mock(ErrorResponse.class);

        when(builder.build(any(), anyString(), any()))
                .thenReturn(fakeResponse);

        var responseEntity = handler.handleAny(ex, request);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getBody()).isEqualTo(fakeResponse);
    }

    @Test
    void handleAny_shouldReturnCustomStatus_forResponseStatusException() {
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        class BadRequestException extends RuntimeException {
            public BadRequestException(String msg) { super(msg); }
        }

        BadRequestException ex = new BadRequestException("Invalid input");
        ErrorResponse fakeResponse = mock(ErrorResponse.class);

        when(builder.build(HttpStatus.BAD_REQUEST, ex.getMessage(), request))
                .thenReturn(fakeResponse);

        var responseEntity = handler.handleAny(ex, request);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo(fakeResponse);
    }

    @Test
    void resolveHttpStatus_shouldReturnInternalServerError_ifNoResponseStatusAnnotation() throws Exception {
        // Reflection pour tester la méthode privée
        Exception ex = new Exception("Error");
        var status = HttpStatus.INTERNAL_SERVER_ERROR;

        // handleAny est déjà testé, donc pas besoin de tester resolveHttpStatus séparément
        var responseEntity = handler.handleAny(ex, request);
        assertThat(responseEntity.getStatusCode()).isEqualTo(status);
    }
}
