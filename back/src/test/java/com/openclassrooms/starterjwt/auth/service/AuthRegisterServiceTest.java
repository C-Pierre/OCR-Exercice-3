package com.openclassrooms.starterjwt.auth.service;

import com.openclassrooms.starterjwt.auth.request.SignupRequest;
import com.openclassrooms.starterjwt.common.exception.BadRequestException;
import com.openclassrooms.starterjwt.common.response.MessageResponse;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthRegisterServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private AuthRegisterService authRegisterService;

    @Test
    void should_register_user_successfully() {
        // GIVEN
        SignupRequest request = new SignupRequest();
        request.setEmail("john.doe@test.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password");

        when(userRepositoryPort.existsByEmail("john.doe@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // WHEN
        MessageResponse response = authRegisterService.execute(request);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("User registered successfully!");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryPort).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo("john.doe@test.com");
        assertThat(savedUser.getFirstName()).isEqualTo("John");
        assertThat(savedUser.getLastName()).isEqualTo("Doe");
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedUser.isAdmin()).isFalse();
    }

    @Test
    void should_throw_exception_when_email_already_exists() {
        // GIVEN
        SignupRequest request = new SignupRequest();
        request.setEmail("existing@test.com");

        when(userRepositoryPort.existsByEmail("existing@test.com")).thenReturn(true);

        // WHEN / THEN
        assertThatThrownBy(() -> authRegisterService.execute(request))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("Error: Email is already taken!");

        verify(userRepositoryPort, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }
}
