package com.openclassrooms.starterjwt.session.model;

import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.user.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class SessionTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validSession_shouldHaveNoViolations() {
        Teacher teacher = Teacher.builder()
                .firstName("Ada")
                .lastName("Lovelace")
                .build();

        Session session = Session.builder()
                .name("Spring Boot Workshop")
                .date(new Date())
                .description("Session description")
                .teacher(teacher)
                .users(List.of())
                .build();

        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        assertThat(violations).isEmpty();
    }

    @Test
    void name_cannotBeBlank_orTooLong() {
        Session session = new Session()
                .setName("")
                .setDate(new Date())
                .setDescription("Desc")
                .setTeacher(new Teacher());

        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("name"));

        session.setName("A".repeat(51));
        violations = validator.validate(session);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void date_cannotBeNull() {
        Session session = new Session()
                .setName("Session")
                .setDescription("Desc")
                .setTeacher(new Teacher());

        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("date"));
    }

    @Test
    void description_cannotBeNull_orTooLong() {
        Session session = new Session()
                .setName("Session")
                .setDate(new Date())
                .setTeacher(new Teacher());

        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("description"));

        session.setDescription("A".repeat(2501));
        violations = validator.validate(session);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("description"));
    }

    @Test
    void equalsAndHashCode_shouldUseIdOnly() {
        Session s1 = new Session();
        s1.setId(1L);
        s1.setName("Session A");

        Session s2 = new Session();
        s2.setId(1L);
        s2.setName("Session B");

        Session s3 = new Session();
        s3.setId(2L);
        s3.setName("Session A");

        assertThat(s1).isEqualTo(s2);
        assertThat(s1).isNotEqualTo(s3);
        assertThat(s1).isNotEqualTo(null);
        assertThat(s1).isNotEqualTo(new Object());
        assertThat(s1).isEqualTo(s1);

        assertThat(s1.hashCode()).isEqualTo(s2.hashCode());
        assertThat(s1.hashCode()).isNotEqualTo(s3.hashCode());
    }

    @Test
    void canEqual_shouldBehaveCorrectly() {
        Session session = new Session();
        session.setId(1L);

        assertThat(session.canEqual(new Session())).isTrue();
        assertThat(session.canEqual(new Object())).isFalse();
    }

    @Test
    void toString_shouldContainUsefulFields() {
        Teacher teacher = Teacher.builder()
                .firstName("Ada")
                .lastName("Lovelace")
                .build();

        Session session = Session.builder()
                .name("Yoga session")
                .description("Relaxing")
                .date(new Date())
                .teacher(teacher)
                .build();

        String toString = session.toString();

        assertThat(toString).contains("Yoga session");
        assertThat(toString).contains("Relaxing");
    }

    @Test
    void chainedSetters_shouldWork() {
        Session session = new Session()
                .setName("Session")
                .setDescription("Desc")
                .setDate(new Date())
                .setTeacher(new Teacher())
                .setUsers(List.of(new User()));

        assertThat(session.getName()).isEqualTo("Session");
        assertThat(session.getUsers()).hasSize(1);
    }

    @Test
    void equalsAndHashCode_shouldHandleNullId() {
        Session s1 = new Session();
        Session s2 = new Session();

        assertThat(s1).isEqualTo(s2);
        assertThat(s1.hashCode()).isEqualTo(s2.hashCode());

        s1.setName("Session");
        s2.setName("Session");

        // toujours égalité car id == null
        assertThat(s1).isEqualTo(s2);
    }

    @Test
    void builder_shouldSetAllFieldsIncludingTechnicalOnes() {
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        Session session = Session.builder()
                .id(99L)
                .name("Full Session")
                .description("Desc")
                .date(new Date())
                .teacher(new Teacher())
                .createdAt(created)
                .updatedAt(updated)
                .build();

        assertThat(session.getId()).isEqualTo(99L);
        assertThat(session.getCreatedAt()).isEqualTo(created);
        assertThat(session.getUpdatedAt()).isEqualTo(updated);
    }

    @Test
    void settersForCreatedAtAndUpdatedAt_shouldWork() {
        LocalDateTime created = LocalDateTime.now().minusHours(2);
        LocalDateTime updated = LocalDateTime.now();

        Session session = new Session()
                .setCreatedAt(created)
                .setUpdatedAt(updated);

        assertThat(session.getCreatedAt()).isEqualTo(created);
        assertThat(session.getUpdatedAt()).isEqualTo(updated);
    }

    @Test
    void toString_shouldContainIdAndTimestamps() {
        LocalDateTime created = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updated = LocalDateTime.of(2024, 1, 2, 12, 0);

        Session session = Session.builder()
                .id(10L)
                .name("Session")
                .description("Desc")
                .date(new Date())
                .teacher(new Teacher())
                .createdAt(created)
                .updatedAt(updated)
                .build();

        String str = session.toString();

        assertThat(str).contains("id=10");
        assertThat(str).contains("createdAt=" + created);
        assertThat(str).contains("updatedAt=" + updated);
    }

    @Test
    void equals_shouldReturnFalse_whenOneIdIsNull() {
        Session s1 = new Session();
        s1.setId(1L);

        Session s2 = new Session();

        assertThat(s1).isNotEqualTo(s2);
    }

    @Test
    void equals_shouldCoverAllBranches_withBuilderAndIdOnly() {
        Session s1 = Session.builder().id(1L).build();
        Session s2 = Session.builder().id(1L).build();
        Session s3 = Session.builder().id(2L).build();
        Session sNullId1 = Session.builder().build();
        Session sNullId2 = Session.builder().build();

        assertThat(s1).isEqualTo(s1);

        assertThat(s1).isEqualTo(s2);

        assertThat(s1).isNotEqualTo(s3);

        assertThat(sNullId1).isEqualTo(sNullId2);

        assertThat(s1).isNotEqualTo(sNullId1);

        assertThat(s1).isNotEqualTo(null);
        assertThat(s1).isNotEqualTo(new Object());
    }

    @Test
    void hashCode_shouldBeBasedOnIdOnly() {
        Session s1 = Session.builder().id(5L).build();
        Session s2 = Session.builder().id(5L).build();
        Session s3 = Session.builder().id(6L).build();
        Session sNullId1 = Session.builder().build();
        Session sNullId2 = Session.builder().build();

        assertThat(s1.hashCode()).isEqualTo(s2.hashCode());
        assertThat(s1.hashCode()).isNotEqualTo(s3.hashCode());
        assertThat(sNullId1.hashCode()).isEqualTo(sNullId2.hashCode());
    }

    @Test
    void toString_shouldContainId_whenBuiltWithBuilder() {
        Session session = Session.builder()
                .id(10L)
                .name("Session Builder")
                .build();

        String str = session.toString();

        assertThat(str).contains("id=10");
        assertThat(str).contains("name=Session Builder");
    }

    @Test
    void canEqual_shouldReturnTrueOnlyForSameType() {
        Session session = Session.builder().id(1L).build();

        assertThat(session.canEqual(Session.builder().build())).isTrue();
        assertThat(session.canEqual(new Object())).isFalse();
    }
}
