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

class CreateSessionRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRequest_shouldHaveNoViolations() {
        CreateSessionRequest request = new CreateSessionRequest();
        request.setName("Spring Boot Workshop");
        request.setDate(new Date());
        request.setTeacherId(1L);
        request.setDescription("Description valide pour la session.");
        request.setUsers(List.of(1L, 2L));

        Set<ConstraintViolation<CreateSessionRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void nameCannotBeBlankOrTooLong() {
        CreateSessionRequest request = new CreateSessionRequest();
        request.setName(""); // blank
        request.setDate(new Date());
        request.setTeacherId(1L);
        request.setDescription("Desc");

        Set<ConstraintViolation<CreateSessionRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));

        request.setName("A".repeat(51)); // >50
        violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void descriptionCannotBeNullOrTooLong() {
        CreateSessionRequest request = new CreateSessionRequest();
        request.setName("Session");
        request.setDate(new Date());
        request.setTeacherId(1L);

        request.setDescription(null);
        Set<ConstraintViolation<CreateSessionRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("description"));

        request.setDescription("A".repeat(2501));
        violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    @Test
    void teacherIdAndDateCannotBeNull() {
        CreateSessionRequest request = new CreateSessionRequest();
        request.setName("Session");
        request.setDescription("Desc");

        Set<ConstraintViolation<CreateSessionRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("teacherId"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("date"));
    }

    @Test
    void gettersAndSetters_shouldWork() {
        Date now = new Date();
        CreateSessionRequest request = new CreateSessionRequest();
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

        CreateSessionRequest req1 = new CreateSessionRequest();
        req1.setName("Session 1");
        req1.setDescription("Desc");
        req1.setTeacherId(1L);
        req1.setDate(now);
        req1.setUsers(List.of(1L, 2L));

        CreateSessionRequest req2 = new CreateSessionRequest();
        req2.setName("Session 1");
        req2.setDescription("Desc");
        req2.setTeacherId(1L);
        req2.setDate(now);
        req2.setUsers(List.of(1L, 2L));

        assertThat(req1).isEqualTo(req2);
        assertThat(req1.hashCode()).isEqualTo(req2.hashCode());

        assertThat(req1).isEqualTo(req1);

        CreateSessionRequest req3 = new CreateSessionRequest();
        req3.setName("Session 2");
        req3.setDescription("Desc");
        req3.setTeacherId(1L);
        req3.setDate(now);

        assertThat(req1).isNotEqualTo(req3);
        assertThat(req1.hashCode()).isNotEqualTo(req3.hashCode());

        CreateSessionRequest req4 = new CreateSessionRequest();
        CreateSessionRequest req5 = new CreateSessionRequest();

        assertThat(req4).isEqualTo(req5);
        assertThat(req4.hashCode()).isEqualTo(req5.hashCode());

        assertThat(req1).isNotEqualTo(null);
        assertThat(req1).isNotEqualTo(new Object());

        assertThat(req1.canEqual(req2)).isTrue();
        assertThat(req1.canEqual(new Object())).isFalse();

        String str = req1.toString();
        assertThat(str).contains("Session 1", "Desc", "1", now.toString());
    }
    @Test
    void equals_fullCoverage() {
        Date now = new Date();

        CreateSessionRequest r1 = new CreateSessionRequest();
        r1.setName("S1");
        r1.setDescription("Desc");
        r1.setTeacherId(1L);
        r1.setDate(now);

        CreateSessionRequest r2 = new CreateSessionRequest();
        r2.setName("S1");
        r2.setDescription("Desc");
        r2.setTeacherId(1L);
        r2.setDate(now);

        CreateSessionRequest r3 = new CreateSessionRequest();
        r3.setName("S2");
        r3.setDescription("Desc");
        r3.setTeacherId(1L);
        r3.setDate(now);

        CreateSessionRequest r4 = new CreateSessionRequest();
        CreateSessionRequest r5 = new CreateSessionRequest();

        assertThat(r1).isEqualTo(r1);

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());

        assertThat(r1).isNotEqualTo(r3);
        assertThat(r1.hashCode()).isNotEqualTo(r3.hashCode());

        assertThat(r4).isEqualTo(r5);
        assertThat(r4.hashCode()).isEqualTo(r5.hashCode());

        assertThat(r1).isNotEqualTo(null);
        assertThat(r1).isNotEqualTo(new Object());

        assertThat(r1.canEqual(r2)).isTrue();
        assertThat(r1.canEqual(new Object())).isFalse();

        assertThat(r1).isNotEqualTo(r4);
    }

}
