package com.openclassrooms.starterjwt.common.error.builder;

import com.openclassrooms.starterjwt.common.error.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ErrorResponseBuilderTest {

    @Test
    void build_shouldCreateErrorResponse() {
        // Arrange
        ErrorResponseBuilder builder = new ErrorResponseBuilder();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test");

        String message = "Something went wrong";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // Act
        ErrorResponse response = builder.build(status, message, request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getTimestamp()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(status.value());
        assertThat(response.getError()).isEqualTo(status.getReasonPhrase());
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getPath()).isEqualTo("/api/test");
    }
}
