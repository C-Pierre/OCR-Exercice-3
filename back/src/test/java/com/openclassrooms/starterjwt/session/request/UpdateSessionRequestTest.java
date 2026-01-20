package com.openclassrooms.starterjwt.session.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

class UpdateSessionRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRequest_shouldHaveNoViolations() {
        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setName("Spring Boot Advanced");
        request.setDate(new Date());
        request.setTeacherId(1L);
        request.setDescription("Description valide de session.");
        request.setUsers(List.of(1L, 2L));

        Set<ConstraintViolation<UpdateSessionRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void nameCannotBeBlankOrTooLong() {
        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setName(""); // blank
        request.setDate(new Date());
        request.setTeacherId(1L);
        request.setDescription("Desc");

        Set<ConstraintViolation<UpdateSessionRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));

        request.setName("A".repeat(51)); // >50
        violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void descriptionCannotBeNullOrTooLong() {
        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setName("Session");
        request.setDate(new Date());
        request.setTeacherId(1L);

        request.setDescription(null);
        Set<ConstraintViolation<UpdateSessionRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("description"));

        request.setDescription("A".repeat(2501));
        violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    @Test
    void teacherIdAndDateCannotBeNull() {
        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setName("Session");
        request.setDescription("Desc");

        Set<ConstraintViolation<UpdateSessionRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("teacherId"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("date"));
    }

    @Test
    void gettersAndSetters_shouldWork() {
        Date now = new Date();
        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setName("Session 1");
        request.setDescription("Desc");
        request.setDate(now);
        request.setTeacherId(5L);
        request.setUsers(List.of(1L, 2L));

        assertThat(request.getName()).isEqualTo("Session 1");
        assertThat(request.getDescription()).isEqualTo("Desc");
        assertThat(request.getDate()).isEqualTo(now);
        assertThat(request.getTeacherId()).isEqualTo(5L);
        assertThat(request.getUsers()).containsExactly(1L, 2L);
    }

    @Test
    void lombokEqualsHashCodeAndToString_fullCoverage() {
        Date now = new Date();

        UpdateSessionRequest req1 = new UpdateSessionRequest();
        req1.setName("Session 1");
        req1.setDescription("Desc");
        req1.setTeacherId(1L);
        req1.setDate(now);
        req1.setUsers(List.of(1L, 2L));

        UpdateSessionRequest req2 = new UpdateSessionRequest();
        req2.setName("Session 1");
        req2.setDescription("Desc");
        req2.setTeacherId(1L);
        req2.setDate(now);
        req2.setUsers(List.of(1L, 2L));

        UpdateSessionRequest req3 = new UpdateSessionRequest();
        req3.setName("Session 2");
        req3.setDescription("Desc");
        req3.setTeacherId(1L);
        req3.setDate(now);

        UpdateSessionRequest req4 = new UpdateSessionRequest();
        UpdateSessionRequest req5 = new UpdateSessionRequest();

        assertThat(req1).isEqualTo(req1);

        assertThat(req1).isEqualTo(req2);
        assertThat(req1.hashCode()).isEqualTo(req2.hashCode());

        assertThat(req1).isNotEqualTo(req3);
        assertThat(req1.hashCode()).isNotEqualTo(req3.hashCode());

        assertThat(req4).isEqualTo(req5);
        assertThat(req4.hashCode()).isEqualTo(req5.hashCode());

        assertThat(req1).isNotEqualTo(null);
        assertThat(req1).isNotEqualTo(new Object());

        assertThat(req1.canEqual(req2)).isTrue();
        assertThat(req1.canEqual(new Object())).isFalse();

        assertThat(req1).isNotEqualTo(req4);

        String str = req1.toString();
        assertThat(str).contains("Session 1", "Desc", "1", now.toString());
    }
}
