package com.openclassrooms.starterjwt.common.error.response;

import lombok.Getter;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}