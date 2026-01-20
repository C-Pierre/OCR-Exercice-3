package com.openclassrooms.starterjwt.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.openclassrooms.starterjwt.auth.request.LoginRequest;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.jwt.response.JwtResponse;
import com.openclassrooms.starterjwt.security.service.UserDetailsImpl;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

class AuthLoginServiceTest {

    @InjectMocks
    private AuthLoginService authLoginService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testExecute_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "test@example.com",
                "John",
                "Doe",
                false,
                "password"
        );

        User user = new User();
        user.setAdmin(true);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");
        when(userRepositoryPort.getByEmail(userDetails.getUsername())).thenReturn(user);

        // Act
        JwtResponse jwtResponse = authLoginService.execute(loginRequest);

        // Assert
        assertNotNull(jwtResponse);
        assertEquals("jwt-token", jwtResponse.getToken());
        assertEquals(userDetails.getId(), jwtResponse.getId());
        assertEquals(userDetails.getUsername(), jwtResponse.getUsername());
        assertEquals(userDetails.getFirstName(), jwtResponse.getFirstName());
        assertEquals(userDetails.getLastName(), jwtResponse.getLastName());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJwtToken(authentication);
        verify(userRepositoryPort).getByEmail(userDetails.getUsername());
    }

    @Test
    void testExecute_UserNotAdmin() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        UserDetailsImpl userDetails = new UserDetailsImpl(
                2L,
                "normal@example.com",
                "Jane",
                "Doe",
                false,
                "password"
        );

        User user = new User();
        user.setAdmin(false);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token-2");
        when(userRepositoryPort.getByEmail(userDetails.getUsername())).thenReturn(user);

        // Act
        JwtResponse jwtResponse = authLoginService.execute(loginRequest);

        // Assert
        assertNotNull(jwtResponse);
        assertFalse(jwtResponse.getAdmin());
    }

    @Test
    void testExecute_AuthenticationFails() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("bad@example.com");
        loginRequest.setPassword("wrong");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authLoginService.execute(loginRequest));
        assertEquals("Authentication failed", exception.getMessage());
    }

    @Test
    void testExecute_userIsNull_shouldSetAdminFalse() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("missing@example.com");
        loginRequest.setPassword("password");

        UserDetailsImpl userDetails = new UserDetailsImpl(
                3L,
                "missing@example.com",
                "Ghost",
                "User",
                false,
                "password"
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token-3");
        when(userRepositoryPort.getByEmail(userDetails.getUsername())).thenReturn(null); // <-- user null

        // Act
        JwtResponse jwtResponse = authLoginService.execute(loginRequest);

        // Assert
        assertNotNull(jwtResponse);
        assertFalse(jwtResponse.getAdmin(), "Admin should be false when user is null");
    }
}
