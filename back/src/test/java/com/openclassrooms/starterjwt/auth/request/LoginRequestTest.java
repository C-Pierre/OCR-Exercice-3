package com.openclassrooms.starterjwt.auth.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ---------------- VALID CASE ----------------

    @Test
    void validRequest_shouldHaveNoViolations() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("Password123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    // ---------------- EMAIL ----------------

    @Test
    void email_cannotBeBlankOrInvalidOrTooLong() {
        LoginRequest request = new LoginRequest();
        request.setPassword("Password123");

        // blank
        request.setEmail("");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));

        // invalid format
        request.setEmail("invalid-email");
        violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));

        // too long
        request.setEmail("a".repeat(51) + "@test.com");
        violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    // ---------------- PASSWORD ----------------

    @Test
    void password_cannotBeBlankOrInvalidSize() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");

        // blank
        request.setPassword("");
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));

        // too short
        request.setPassword("123");
        violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));

        // too long
        request.setPassword("a".repeat(121));
        violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    // ---------------- GETTERS / SETTERS ----------------

    @Test
    void gettersAndSetters_shouldWork() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("Password123");

        assertThat(request.getEmail()).isEqualTo("test@test.com");
        assertThat(request.getPassword()).isEqualTo("Password123");
    }

    // ---------------- LOMBOK EQUALS / HASHCODE / TOSTRING ----------------

    @Test
    void lombokEqualsHashCodeAndToString_fullCoverage() {
        LoginRequest r1 = new LoginRequest();
        r1.setEmail("test@test.com");
        r1.setPassword("Password123");

        LoginRequest r2 = new LoginRequest();
        r2.setEmail("test@test.com");
        r2.setPassword("Password123");

        LoginRequest r3 = new LoginRequest();
        r3.setEmail("other@test.com");
        r3.setPassword("Password123");

        LoginRequest r4 = new LoginRequest();
        LoginRequest r5 = new LoginRequest();

        // reflexivity
        assertThat(r1).isEqualTo(r1);

        // same values
        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());

        // different values
        assertThat(r1).isNotEqualTo(r3);
        assertThat(r1.hashCode()).isNotEqualTo(r3.hashCode());

        // null fields
        assertThat(r4).isEqualTo(r5);
        assertThat(r4.hashCode()).isEqualTo(r5.hashCode());

        // null and other type
        assertThat(r1).isNotEqualTo(null);
        assertThat(r1).isNotEqualTo(new Object());

        // canEqual
        assertThat(r1.canEqual(r2)).isTrue();
        assertThat(r1.canEqual(new Object())).isFalse();

        // mixed null / non-null
        assertThat(r1).isNotEqualTo(r4);

        // toString
        String toString = r1.toString();
        assertThat(toString).contains("test@test.com", "Password123");
    }
}
