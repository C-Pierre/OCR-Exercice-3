package com.openclassrooms.starterjwt.auth.request;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

@Data
public class SignupRequest {
    @NotBlank(message = "Email must not be blank")
    @Size(max = 50, message = "Email must be 50 characters max")
    @Email(message = "Email format is invalid")
    private String email;

    @NotBlank(message = "FirstName must not be blank")
    @Size(min = 3, max = 20, message = "FirstName must be between 3 and 20 characters")
    private String firstName;

    @NotBlank(message = "Lastname must not be blank")
    @Size(min = 3, max = 20, message = "Lastname must be between 3 and 20 characters")
    private String lastName;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 120, message = "Password must be between 6 and 120 characters")
    private String password;
}
