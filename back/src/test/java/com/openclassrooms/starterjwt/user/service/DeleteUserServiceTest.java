package com.openclassrooms.starterjwt.user.service;

import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;
import com.openclassrooms.starterjwt.common.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class DeleteUserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private DeleteUserService deleteUserService;

    @Test
    void should_delete_user_when_email_matches() {
        Long userId = 1L;
        String email = "test@example.com";

        User user = new User();
        user.setId(userId);
        user.setEmail(email);

        when(userRepositoryPort.getById(userId)).thenReturn(user);

        deleteUserService.execute(userId, email);

        verify(userRepositoryPort).getById(userId);
        verify(userRepositoryPort).delete(user);
    }

    @Test
    void should_throw_unauthorized_exception_when_email_does_not_match() {
        Long userId = 2L;
        String authenticatedEmail = "wrong@example.com";

        User user = new User();
        user.setId(userId);
        user.setEmail("actual@example.com");

        when(userRepositoryPort.getById(userId)).thenReturn(user);

        assertThatThrownBy(() -> deleteUserService.execute(userId, authenticatedEmail))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessageContaining("You are not allowed to delete this user");

        verify(userRepositoryPort).getById(userId);
        verify(userRepositoryPort, never()).delete(any());
    }
}
