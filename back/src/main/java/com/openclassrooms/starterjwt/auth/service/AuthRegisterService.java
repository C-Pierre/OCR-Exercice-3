package com.openclassrooms.starterjwt.auth.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.auth.request.SignupRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.openclassrooms.starterjwt.common.response.MessageResponse;
import com.openclassrooms.starterjwt.common.exception.BadRequestException;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;

@Service
public class AuthRegisterService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepositoryPort userRepositoryPort;

    public AuthRegisterService(
        PasswordEncoder passwordEncoder,
        UserRepositoryPort userRepositoryPort
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepositoryPort = userRepositoryPort;
    }

    public MessageResponse execute(SignupRequest signUpRequest) {
        if (userRepositoryPort.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Error: Email is already taken!");
        }

        User user = User.builder()
            .email(signUpRequest.getEmail())
            .lastName(signUpRequest.getLastName())
            .firstName(signUpRequest.getFirstName())
            .password(passwordEncoder.encode(signUpRequest.getPassword()))
            .admin(false)
            .build();

        userRepositoryPort.save(user);

        return new MessageResponse("User registered successfully!");
    }
}
