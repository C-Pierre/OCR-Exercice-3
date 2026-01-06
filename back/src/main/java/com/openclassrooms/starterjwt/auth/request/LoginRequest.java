package com.openclassrooms.starterjwt.auth.request;

import lombok.Data;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank(message = "Email must not be blank")
    @Size(max = 50, message = "Email must be 50 characters max")
    @Email(message = "Email format is invalid")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 120, message = "Password must be between 6 and 120 characters")
    private String password;
}
