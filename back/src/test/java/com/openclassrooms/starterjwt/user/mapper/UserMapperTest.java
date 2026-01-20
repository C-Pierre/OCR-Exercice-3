package com.openclassrooms.starterjwt.user.mapper;

import java.util.List;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    // ---------- ENTITY → DTO ----------

    @Test
    void toDto_shouldMapAllFields_exceptPassword() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAdmin(true);
        user.setPassword("secret"); // doit être ignoré
        user.setCreatedAt(LocalDateTime.now().minusDays(1));
        user.setUpdatedAt(LocalDateTime.now());

        UserDto dto = userMapper.toDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getEmail()).isEqualTo("test@example.com");
        assertThat(dto.getFirstName()).isEqualTo("John");
        assertThat(dto.getLastName()).isEqualTo("Doe");
        assertThat(dto.isAdmin()).isTrue();
        assertThat(dto.getCreatedAt()).isEqualTo(user.getCreatedAt());
        assertThat(dto.getUpdatedAt()).isEqualTo(user.getUpdatedAt());
    }

    @Test
    void toDto_shouldReturnNullWhenUserIsNull() {
        UserDto dto = userMapper.toDto((User) null);
        assertThat(dto).isNull();
    }

    @Test
    void toDtoList_shouldMapAllUsers() {
        User u1 = new User();
        u1.setId(1L);
        u1.setEmail("a@test.com");
        User u2 = new User();
        u2.setId(2L);
        u2.setEmail("b@test.com");

        List<UserDto> dtos = userMapper.toDto(List.of(u1, u2));

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getId()).isEqualTo(1L);
        assertThat(dtos.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void toDtoList_shouldReturnEmptyWhenListIsNull() {
        List<UserDto> dtos = userMapper.toDto((List<User>) null);
        assertThat(dtos).isNotNull().isEmpty();
    }

    // ---------- DTO → ENTITY ----------

    @Test
    void toEntity_shouldMapAllFields_exceptPassword() {
        UserDto dto = new UserDto(2L, "jane@example.com", "Doe", "Jane", false, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusHours(1));
        User user = userMapper.toEntity(dto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getEmail()).isEqualTo("jane@example.com");
        assertThat(user.getFirstName()).isEqualTo("Jane");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.isAdmin()).isFalse();
        assertThat(user.getPassword()).isNull();
    }

    @Test
    void toEntity_shouldReturnNullWhenDtoIsNull() {
        User user = userMapper.toEntity((UserDto) null);
        assertThat(user).isNull();
    }

    @Test
    void toEntityList_shouldMapAllDtos_withoutSettingPassword() {
        UserDto d1 = new UserDto(10L, "x@test.com", "X", "X", false, null, null);
        UserDto d2 = new UserDto(11L, "y@test.com", "Y", "Y", true, null, null);

        List<User> users = userMapper.toEntity(List.of(d1, d2));

        assertThat(users).hasSize(2);
        assertThat(users.get(0).getId()).isEqualTo(10L);
        assertThat(users.get(0).getPassword()).isNull();
        assertThat(users.get(1).getId()).isEqualTo(11L);
        assertThat(users.get(1).getPassword()).isNull();
    }

    @Test
    void toEntityList_shouldReturnEmptyWhenListIsNull() {
        List<User> users = userMapper.toEntity((List<UserDto>) null);
        assertThat(users).isNotNull().isEmpty();
    }

    @Test
    void toEntity_shouldNotThrowException_whenCalled() {
        UserDto dto = new UserDto(99L, "safe@test.com", "Safe", "Test", false, null, null);
        assertThatCode(() -> userMapper.toEntity(dto)).doesNotThrowAnyException();
    }
}
