package com.openclassrooms.starterjwt.security.service;

import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private UserDetailsServiceImpl service;

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@test.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("secret");

        when(userRepositoryPort.getByEmail("john@test.com")).thenReturn(user);

        UserDetails result = service.loadUserByUsername("john@test.com");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("john@test.com");
        assertThat(result.getPassword()).isEqualTo("secret");

        verify(userRepositoryPort).getByEmail("john@test.com");
    }
}
