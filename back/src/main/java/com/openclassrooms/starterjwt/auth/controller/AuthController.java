package com.openclassrooms.starterjwt.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.starterjwt.security.jwt.response.JwtResponse;
import com.openclassrooms.starterjwt.auth.request.LoginRequest;
import com.openclassrooms.starterjwt.auth.request.SignupRequest;
import com.openclassrooms.starterjwt.auth.service.AuthLoginService;
import com.openclassrooms.starterjwt.common.response.MessageResponse;
import com.openclassrooms.starterjwt.auth.service.AuthRegisterService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthLoginService authLoginService;
    private final AuthRegisterService authRegisterService;

    public AuthController(
        AuthLoginService authLoginService,
        AuthRegisterService authRegisterService
    ) {
        this.authLoginService = authLoginService;
        this.authRegisterService = authRegisterService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authLoginService.execute(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return ResponseEntity.ok(authRegisterService.execute(signUpRequest));
    }
}
