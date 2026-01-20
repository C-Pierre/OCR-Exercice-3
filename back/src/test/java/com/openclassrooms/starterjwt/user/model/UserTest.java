package com.openclassrooms.starterjwt.user.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Set;
import static org.assertj.core.api.Assertions.*;

class UserTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validUser_shouldHaveNoViolations() {
        User user = User.builder()
                .email("user@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("Password123")
                .admin(false)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).isEmpty();
    }

    @Test
    void email_mustBeValidAndNotNull() {
        User user = new User()
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("Password123");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void sizeConstraints_shouldBeValidated() {
        User user = new User()
                .setEmail("a".repeat(51) + "@test.com")
                .setFirstName("A".repeat(21))
                .setLastName("B".repeat(21))
                .setPassword("C".repeat(121));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    void equalsAndHashCode_shouldUseIdOnly() {
        User u1 = new User();
        u1.setId(1L);
        u1.setEmail("a@test.com");

        User u2 = new User();
        u2.setId(1L);
        u2.setEmail("b@test.com");

        User u3 = new User();
        u3.setId(2L);
        u3.setEmail("a@test.com");

        assertThat(u1).isEqualTo(u2);
        assertThat(u1).isNotEqualTo(u3);
        assertThat(u1).isNotEqualTo(null);
        assertThat(u1).isNotEqualTo(new Object());
        assertThat(u1).isEqualTo(u1);

        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());
        assertThat(u1.hashCode()).isNotEqualTo(u3.hashCode());
    }

    @Test
    void canEqual_shouldBehaveCorrectly() {
        User user = new User();
        user.setId(1L);

        assertThat(user.canEqual(new User())).isTrue();
        assertThat(user.canEqual(new Object())).isFalse();
    }

    @Test
    void toString_shouldContainUsefulFields() {
        User user = User.builder()
                .email("user@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("Password")
                .admin(true)
                .build();

        String toString = user.toString();

        assertThat(toString).contains("user@test.com");
        assertThat(toString).contains("John");
        assertThat(toString).contains("Doe");
        assertThat(toString).contains("admin");
    }

    @Test
    void chainedSetters_shouldWork() {
        User user = new User()
                .setEmail("user@test.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("Password")
                .setAdmin(true);

        assertThat(user.getEmail()).isEqualTo("user@test.com");
        assertThat(user.isAdmin()).isTrue();
    }

    @Test
    void equals_shouldReturnTrue_whenBothIdsAreNull_sameInstance() {
        User user = new User();
        assertThat(user).isEqualTo(user);
    }

    @Test
    void hashCode_shouldBeSame_whenIdsAreEqual() {
        User u1 = new User().setId(10L);
        User u2 = new User().setId(10L);

        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());
    }

    @Test
    void builder_shouldCorrectlySetAllFieldsIncludingId() {
        User user = User.builder()
                .id(42L)
                .email("builder@test.com")
                .firstName("Builder")
                .lastName("User")
                .password("secret")
                .admin(true)
                .build();

        assertThat(user.getId()).isEqualTo(42L);
        assertThat(user.getEmail()).isEqualTo("builder@test.com");
        assertThat(user.isAdmin()).isTrue();
    }

    @Test
    void createdAtSetter_shouldBeSetCorrectly() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        user.setCreatedAt(now);

        assertThat(user.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void updatedAtSetter_shouldBeSetCorrectly() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        user.setUpdatedAt(now);

        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void toString_shouldContainId_whenPresent() {
        User user = User.builder()
                .id(99L)
                .email("user@test.com")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .build();

        String str = user.toString();

        assertThat(str).contains("id=99");
        assertThat(str).contains("user@test.com");
        assertThat(str).contains("John");
    }

    @Test
    void builder_shouldSetAllFieldsIncludingIdAndTimestamps() {
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        User user = User.builder()
                .id(10L)
                .email("builder@test.com")
                .firstName("Builder")
                .lastName("User")
                .password("secret")
                .admin(true)
                .createdAt(created)
                .updatedAt(updated)
                .build();

        assertThat(user.getId()).isEqualTo(10L);
        assertThat(user.getEmail()).isEqualTo("builder@test.com");
        assertThat(user.getFirstName()).isEqualTo("Builder");
        assertThat(user.getLastName()).isEqualTo("User");
        assertThat(user.getPassword()).isEqualTo("secret");
        assertThat(user.isAdmin()).isTrue();
        assertThat(user.getCreatedAt()).isEqualTo(created);
        assertThat(user.getUpdatedAt()).isEqualTo(updated);
    }

    @Test
    void equals_shouldReturnTrue_whenIdsAreEqual() {
        User u1 = User.builder().id(5L).build();
        User u2 = User.builder().id(5L).build();
        assertThat(u1).isEqualTo(u2);
    }

    @Test
    void equals_shouldReturnFalse_whenIdsAreDifferent() {
        User u1 = User.builder().id(1L).build();
        User u2 = User.builder().id(2L).build();
        assertThat(u1).isNotEqualTo(u2);
    }

    @Test
    void equals_shouldReturnFalse_whenOneIdIsNull() {
        User u1 = User.builder().id(null).build();
        User u2 = User.builder().id(1L).build();
        assertThat(u1).isNotEqualTo(u2);
        assertThat(u2).isNotEqualTo(u1);
    }

    @Test
    void equals_shouldReturnTrue_whenSameInstance() {
        User u = User.builder().id(null).build();
        assertThat(u).isEqualTo(u);
    }

    @Test
    void equals_shouldReturnFalse_whenComparedToNullOrOtherClass() {
        User u = User.builder().id(1L).build();
        assertThat(u).isNotEqualTo(null);
        assertThat(u).isNotEqualTo("string");
    }

    @Test
    void hashCode_shouldBeEqual_whenIdsAreEqual() {
        User u1 = User.builder().id(42L).build();
        User u2 = User.builder().id(42L).build();
        assertThat(u1.hashCode()).isEqualTo(u2.hashCode());
    }

    @Test
    void hashCode_shouldDiffer_whenIdsAreDifferent() {
        User u1 = User.builder().id(1L).build();
        User u2 = User.builder().id(2L).build();
        assertThat(u1.hashCode()).isNotEqualTo(u2.hashCode());
    }

    @Test
    void hashCode_shouldNotThrow_whenIdIsNull() {
        User u = User.builder().id(null).build();
        assertThatCode(() -> u.hashCode()).doesNotThrowAnyException();
    }

    @Test
    void setCreatedAtAndUpdatedAt_shouldWork_viaBuilder() {
        LocalDateTime created = LocalDateTime.now().minusDays(2);
        LocalDateTime updated = LocalDateTime.now();

        User user = User.builder()
                .createdAt(created)
                .updatedAt(updated)
                .build();

        assertThat(user.getCreatedAt()).isEqualTo(created);
        assertThat(user.getUpdatedAt()).isEqualTo(updated);
    }

    @Test
    void toString_shouldContainIdAndOtherFields() {
        User user = User.builder()
                .id(99L)
                .email("user@test.com")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .build();

        String str = user.toString();
        assertThat(str).contains("id=99");
        assertThat(str).contains("user@test.com");
        assertThat(str).contains("John");
        assertThat(str).contains("Doe");
        assertThat(str).contains("admin=true");
    }
}
