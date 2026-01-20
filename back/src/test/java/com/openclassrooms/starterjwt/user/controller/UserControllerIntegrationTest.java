package com.openclassrooms.starterjwt.user.controller;

import com.openclassrooms.starterjwt.session.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.MockMvc;
import com.openclassrooms.starterjwt.user.model.User;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.starterjwt.user.repository.UserRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
        user.setPassword("password");
        user.setAdmin(false);

        userRepository.findByEmail(user.getEmail())
            .ifPresent(userRepository::delete);

        savedUser = userRepository.save(user);
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void findById_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/user/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "USER")
    void delete_shouldDeleteUser_whenAuthenticatedUserIsProvided() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", savedUser.getId()))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(savedUser.getId())).isEmpty();
    }
}
