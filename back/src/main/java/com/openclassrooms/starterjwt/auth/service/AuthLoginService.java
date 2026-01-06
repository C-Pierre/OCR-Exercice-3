package com.openclassrooms.starterjwt.auth.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.user.model.User;
import org.springframework.security.core.Authentication;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.jwt.JwtResponse;
import com.openclassrooms.starterjwt.auth.request.LoginRequest;
import com.openclassrooms.starterjwt.security.service.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
public class AuthLoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepositoryPort userRepositoryPort;

    public AuthLoginService(
        AuthenticationManager authenticationManager,
        JwtUtils jwtUtils,
        UserRepositoryPort userRepositoryPort
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepositoryPort = userRepositoryPort;
    }

    public JwtResponse execute(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userRepositoryPort.getByEmail(userDetails.getUsername());

        boolean isAdmin = user != null && user.isAdmin();

        return new JwtResponse(
            jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getFirstName(),
            userDetails.getLastName(),
            isAdmin
        );
    }
}
