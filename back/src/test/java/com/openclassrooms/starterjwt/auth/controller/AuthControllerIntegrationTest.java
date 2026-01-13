package com.openclassrooms.starterjwt.auth.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.user.model.User;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.starterjwt.auth.request.LoginRequest;
import com.openclassrooms.starterjwt.auth.request.SignupRequest;
import com.openclassrooms.starterjwt.user.repository.UserRepository;
import com.openclassrooms.starterjwt.session.repository.SessionRepository;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAllSessionsParticipations();

        User user = new User();
        user.setEmail("test@test.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("Password");
        user.setAdmin(false);

        userRepository.findByEmail(user.getEmail())
            .ifPresent(userRepository::delete);

        savedUser = userRepository.save(user);
    }

    @AfterEach
    void setDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void register_shouldCreateNewUser() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setEmail("newuser@test.com");
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setPassword("Password123");

        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("User registered successfully!"));

        assertThat(userRepository.findByEmail("newuser@test.com")).isPresent();
    }

    @Test
    void login_shouldReturnJwtToken() throws Exception {
        LoginRequest login = new LoginRequest();
        login.setEmail("newuser@test.com");
        login.setPassword("Password123");

        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty())
            .andExpect(jsonPath("$.username").value("newuser@test.com"));
    }

    @Test
    void register_shouldFailForDuplicateEmail() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setEmail("duplicate@test.com");
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setPassword("Password123");

        // 1️⃣ Premier enregistrement → succès
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        // 2️⃣ Deuxième enregistrement avec le même email → doit échouer
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void login_shouldFailWithWrongPassword() throws Exception {
        LoginRequest login = new LoginRequest();
        login.setEmail(savedUser.getEmail());
        login.setPassword("IncorrectPassword");

        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(login)))
            .andExpect(status().isInternalServerError());
    }
}
