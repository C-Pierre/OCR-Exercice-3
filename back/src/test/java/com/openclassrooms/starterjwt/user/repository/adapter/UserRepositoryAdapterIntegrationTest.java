package com.openclassrooms.starterjwt.user.repository.adapter;

import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryAdapterIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindAll_shouldWork() {
        User u1 = new User();
        u1.setEmail("a@test.com");
        u1.setFirstName("John");
        u1.setLastName("Doe");
        u1.setPassword("secret");

        User u2 = new User();
        u2.setEmail("b@test.com");
        u2.setFirstName("Alice");
        u2.setLastName("Smith");
        u2.setPassword("secret");

        userRepository.save(u1);
        userRepository.save(u2);

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void getByEmail_shouldReturnSavedUser() {
        User u = new User();
        u.setEmail("c@test.com");
        u.setFirstName("Grace");
        u.setLastName("Hopper");
        u.setPassword("secret");

        userRepository.save(u);

        User found = userRepository.findByEmail("c@test.com").orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getFirstName()).isEqualTo("Grace");
    }

    @Test
    void existsByEmail_shouldReturnTrueIfExists() {
        User u = new User();
        u.setEmail("d@test.com");
        u.setFirstName("Alan");
        u.setLastName("Turing");
        u.setPassword("secret");
        userRepository.save(u);

        Boolean exists = userRepository.existsByEmail("d@test.com");
        assertThat(exists).isTrue();
    }
}
