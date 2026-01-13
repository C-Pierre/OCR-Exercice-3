package com.openclassrooms.starterjwt.user.repository;

import com.openclassrooms.starterjwt.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_shouldPopulateAuditFields() {
        User user = User.builder()
                .email("audit@test.com")
                .firstName("Audit")
                .lastName("User")
                .password("Password")
                .admin(false)
                .build();

        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void save_shouldFailForDuplicateEmail() {
        User u1 = User.builder()
                .email("duplicate@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("Password")
                .build();

        User u2 = User.builder()
                .email("duplicate@test.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("Password")
                .build();

        userRepository.save(u1);

        assertThatThrownBy(() -> userRepository.saveAndFlush(u2))
                .isInstanceOf(Exception.class);
    }
}
