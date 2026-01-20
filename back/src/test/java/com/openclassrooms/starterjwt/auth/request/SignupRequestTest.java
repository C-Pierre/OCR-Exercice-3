package com.openclassrooms.starterjwt.auth.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

class SignupRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRequest_shouldHaveNoViolations() {
        SignupRequest request = new SignupRequest();
        request.setEmail("user@test.com");
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setPassword("Password123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void email_firstName_lastName_password_constraints() {
        SignupRequest request = new SignupRequest();

        request.setEmail("");
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setPassword("Password123");
        assertThat(validator.validate(request))
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));

        request.setEmail("invalid-email");
        assertThat(validator.validate(request))
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));

        request.setEmail("a".repeat(51) + "@test.com");
        assertThat(validator.validate(request))
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));

        request.setEmail("user@test.com");
        request.setFirstName("");
        assertThat(validator.validate(request))
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));

        request.setFirstName("Al");
        assertThat(validator.validate(request))
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));

        request.setFirstName("A".repeat(21));
        assertThat(validator.validate(request))
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));

        request.setFirstName("Alice");
        request.setLastName("");
        assertThat(validator.validate(request))
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));

        request.setLastName("Sm");
        assertThat(validator.validate(request))
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));

        request.setLastName("A".repeat(21));
        assertThat(validator.validate(request))
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));

        request.setLastName("Smith");
        request.setPassword("");
        assertThat(validator.validate(request))
                .anyMatch(v -> v.getPropertyPath().toString().equals("password"));

        request.setPassword("123");
        assertThat(validator.validate(request))
                .anyMatch(v -> v.getPropertyPath().toString().equals("password"));

        request.setPassword("a".repeat(121));
        assertThat(validator.validate(request))
                .anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void gettersAndSetters_shouldWork() {
        SignupRequest request = new SignupRequest();
        request.setEmail("user@test.com");
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setPassword("Password123");

        assertThat(request.getEmail()).isEqualTo("user@test.com");
        assertThat(request.getFirstName()).isEqualTo("Alice");
        assertThat(request.getLastName()).isEqualTo("Smith");
        assertThat(request.getPassword()).isEqualTo("Password123");
    }

    @Test
    void lombokEqualsHashCodeAndToString_fullCoverage() {
        SignupRequest r1 = new SignupRequest();
        r1.setEmail("user@test.com");
        r1.setFirstName("Alice");
        r1.setLastName("Smith");
        r1.setPassword("Password123");

        SignupRequest r2 = new SignupRequest();
        r2.setEmail("user@test.com");
        r2.setFirstName("Alice");
        r2.setLastName("Smith");
        r2.setPassword("Password123");

        SignupRequest r3 = new SignupRequest();
        r3.setEmail("other@test.com");
        r3.setFirstName("Bob");
        r3.setLastName("Jones");
        r3.setPassword("Password123");

        SignupRequest r4 = new SignupRequest();
        SignupRequest r5 = new SignupRequest();

        assertThat(r1).isEqualTo(r1);

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());

        assertThat(r1).isNotEqualTo(r3);
        assertThat(r1.hashCode()).isNotEqualTo(r3.hashCode());

        assertThat(r4).isEqualTo(r5);
        assertThat(r4.hashCode()).isEqualTo(r5.hashCode());

        assertThat(r1).isNotEqualTo(null);
        assertThat(r1).isNotEqualTo(new Object());

        assertThat(r1).isNotEqualTo(r4);

        assertThat(r1.canEqual(r2)).isTrue();
        assertThat(r1.canEqual(new Object())).isFalse();

        String toString = r1.toString();
        assertThat(toString).contains("user@test.com", "Alice", "Smith");
    }
    @Test
    void lombokEqualsHashCodeAndCanEqual_shouldBeFullyCovered() {
        SignupRequest r1 = new SignupRequest();
        r1.setEmail("user@test.com");
        r1.setFirstName("Alice");
        r1.setLastName("Smith");
        r1.setPassword("Password123");

        SignupRequest r2 = new SignupRequest();
        r2.setEmail("user@test.com");
        r2.setFirstName("Alice");
        r2.setLastName("Smith");
        r2.setPassword("Password123");

        SignupRequest r3 = new SignupRequest();
        r3.setEmail("other@test.com");
        r3.setFirstName("Bob");
        r3.setLastName("Jones");
        r3.setPassword("Password123");

        SignupRequest r4 = new SignupRequest();
        SignupRequest r5 = new SignupRequest();

        assertThat(r1).isEqualTo(r1);              // this == o
        assertThat(r1).isEqualTo(r2);              // mêmes valeurs
        assertThat(r1).isNotEqualTo(r3);           // valeurs différentes
        assertThat(r1).isNotEqualTo(null);         // o == null
        assertThat(r1).isNotEqualTo(new Object()); // autre type

        assertThat(r1).isNotEqualTo(r4);

        assertThat(r4).isEqualTo(r5);

        assertThat(r1.canEqual(r2)).isTrue();
        assertThat(r1.canEqual(new Object())).isFalse();

        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
        assertThat(r1.hashCode()).isNotEqualTo(r3.hashCode());
        assertThat(r4.hashCode()).isEqualTo(r5.hashCode());

        String str = r1.toString();
        assertThat(str).contains("user@test.com");
        assertThat(str).contains("Alice");
        assertThat(str).contains("Smith");
    }
}
