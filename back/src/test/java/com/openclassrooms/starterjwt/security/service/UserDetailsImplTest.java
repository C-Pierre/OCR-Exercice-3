package com.openclassrooms.starterjwt.security.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailsImplTest {

    @Test
    void builder_shouldCreateCorrectUser() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("john@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("secret")
                .admin(true)
                .build();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("john@test.com");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getPassword()).isEqualTo("secret");
        assertThat(user.getAdmin()).isTrue();
    }

    @Test
    void authorities_shouldBeEmpty() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertThat(authorities).isNotNull().isEmpty();
    }

    @Test
    void accountFlags_shouldAlwaysBeTrue() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();
        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void equals_shouldReturnTrue_whenSameObject() {
        UserDetailsImpl user = UserDetailsImpl.builder().id(1L).build();
        assertThat(user).isEqualTo(user); // this == o
    }

    @Test
    void equals_shouldReturnFalse_whenNullOrDifferentClass() {
        UserDetailsImpl user = UserDetailsImpl.builder().id(1L).build();
        assertThat(user).isNotEqualTo(null);
        assertThat(user).isNotEqualTo("string"); // getClass() != o.getClass()
    }

    @Test
    void equals_shouldReturnTrue_whenIdsAreEqual() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(1L).build();
        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void equals_shouldReturnFalse_whenIdsAreDifferent() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(2L).build();
        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void hashCode_shouldBeConsistentWithEquals() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().id(5L).build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(5L).build();
        UserDetailsImpl user3 = UserDetailsImpl.builder().id(6L).build();

        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
    }

    @Test
    void toString_shouldReturnNonEmptyStringContainingFields() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(42L)
                .username("foo@test.com")
                .firstName("Foo")
                .lastName("Bar")
                .password("pw")
                .admin(true)
                .build();

        String str = user.toString();
        assertThat(str).isNotNull();
        assertThat(str).contains("id=42", "username=foo@test.com", "firstName=Foo", "lastName=Bar", "admin=true");
    }

    @Test
    void equals_shouldReturnTrue_whenBothIdsAreNull() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().build();

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void equals_shouldReturnFalse_whenOneIdIsNull() {
        UserDetailsImpl userWithId = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl userWithoutId = UserDetailsImpl.builder().build();

        assertThat(userWithId).isNotEqualTo(userWithoutId);
        assertThat(userWithoutId).isNotEqualTo(userWithId);
    }

    @Test
    void hashCode_shouldWorkWhenIdIsNull() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().build();

        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }
}
