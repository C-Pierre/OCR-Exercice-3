package com.openclassrooms.starterjwt.user.repository.adapter;

import com.openclassrooms.starterjwt.common.exception.NotFoundException;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class UserRepositoryAdapterUnitTest {

    private UserRepository userRepository;
    private UserRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        adapter = new UserRepositoryAdapter(userRepository);
    }

    @Test
    void getById_shouldReturnUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = adapter.getById(1L);
        assertThat(result).isEqualTo(user);
    }

    @Test
    void getById_shouldThrowNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adapter.getById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User not found with id");
    }

    @Test
    void getByEmail_shouldReturnUser() {
        User user = new User();
        user.setEmail("a@test.com");
        when(userRepository.findByEmail("a@test.com")).thenReturn(Optional.of(user));

        User result = adapter.getByEmail("a@test.com");
        assertThat(result).isEqualTo(user);
    }

    @Test
    void getByEmail_shouldThrowNotFound() {
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adapter.getByEmail("unknown@test.com"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User not found with email");
    }

    @Test
    void existsByEmail_shouldReturnCorrectValue() {
        when(userRepository.existsByEmail("a@test.com")).thenReturn(true);

        Boolean exists = adapter.existsByEmail("a@test.com");
        assertThat(exists).isTrue();
    }

    @Test
    void save_shouldCallRepository() {
        User user = new User();
        adapter.save(user);
        verify(userRepository).save(user);
    }

    @Test
    void delete_shouldCallRepository() {
        User user = new User();
        adapter.delete(user);
        verify(userRepository).delete(user);
    }

    @Test
    void findAll_shouldReturnAll() {
        List<User> list = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(list);

        List<User> result = adapter.findAll();
        assertThat(result).hasSize(2);
    }

    @Test
    void findAllById_shouldReturnCorrectList() {
        List<User> list = List.of(new User(), new User());
        when(userRepository.findAllById(List.of(1L, 2L))).thenReturn(list);

        List<User> result = adapter.findAllById(List.of(1L, 2L));
        assertThat(result).hasSize(2);
    }
}
