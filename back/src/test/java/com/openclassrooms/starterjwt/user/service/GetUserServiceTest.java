package com.openclassrooms.starterjwt.user.service;

import com.openclassrooms.starterjwt.user.dto.UserDto;
import com.openclassrooms.starterjwt.user.mapper.UserMapper;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class GetUserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private GetUserService getUserService;

    @Test
    void should_return_user_dto_when_user_exists() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        UserDto userDto = new UserDto();

        when(userRepositoryPort.getById(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = getUserService.execute(userId);

        assertThat(result).isEqualTo(userDto);
        verify(userRepositoryPort).getById(userId);
        verify(userMapper).toDto(user);
    }

    @Test
    void should_throw_exception_when_user_not_found() {
        Long userId = 99L;
        when(userRepositoryPort.getById(userId))
            .thenThrow(new RuntimeException("User not found"));

        assertThatThrownBy(() -> getUserService.execute(userId))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("User not found");

        verify(userRepositoryPort).getById(userId);
        verifyNoInteractions(userMapper);
    }
}
