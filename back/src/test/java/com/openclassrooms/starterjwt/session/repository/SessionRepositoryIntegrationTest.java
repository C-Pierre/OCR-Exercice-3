package com.openclassrooms.starterjwt.session.repository;

import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.teacher.repository.TeacherRepository;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.user.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class SessionRepositoryIntegrationTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    private Teacher savedTeacher;

    @BeforeEach
    void setUp() {
        // Crée un teacher pour les tests
        savedTeacher = teacherRepository.save(
                new Teacher().setFirstName("Ada").setLastName("Lovelace")
        );
    }

    // ---------- CAS NORMAL ----------

    @Test
    void save_shouldPopulateAuditFields() {
        Session session = Session.builder()
                .name("Spring Boot Workshop")
                .description("Description valide")
                .date(new Date())
                .teacher(savedTeacher)
                .users(new ArrayList<>())
                .build();

        Session saved = sessionRepository.save(session);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    // ---------- CONSTRAINTS NOT NULL ----------

    @Test
    void save_shouldFailWhenTeacherIsNull() {
        Session session = Session.builder()
                .name("Session Name")
                .description("Description")
                .date(new Date())
                .teacher(null)
                .build();

        assertThatThrownBy(() -> sessionRepository.saveAndFlush(session))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_shouldFailWhenDateIsNull() {
        Session session = Session.builder()
                .name("Session Name")
                .description("Description")
                .date(null)
                .teacher(savedTeacher)
                .build();

        assertThatThrownBy(() -> sessionRepository.saveAndFlush(session))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void save_shouldFailWhenNameIsBlank() {
        Session session = Session.builder()
                .name("") // blank
                .description("Description")
                .date(new Date())
                .teacher(savedTeacher)
                .build();

        assertThatThrownBy(() -> sessionRepository.saveAndFlush(session))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void save_shouldFailWhenDescriptionIsBlank() {
        Session session = Session.builder()
                .name("Valid Name")
                .description(null) // null
                .date(new Date())
                .teacher(savedTeacher)
                .build();

        assertThatThrownBy(() -> sessionRepository.saveAndFlush(session))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void save_shouldFailWhenNameTooLong() {
        Session session = Session.builder()
                .name("A".repeat(51)) // > 50 caractères
                .description("Valid description")
                .date(new Date())
                .teacher(savedTeacher)
                .build();

        assertThatThrownBy(() -> sessionRepository.saveAndFlush(session))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void save_shouldFailWhenDescriptionTooLong() {
        Session session = Session.builder()
                .name("Valid Name")
                .description("A".repeat(2501)) // > 2500 caractères
                .date(new Date())
                .teacher(savedTeacher)
                .build();

        assertThatThrownBy(() -> sessionRepository.saveAndFlush(session))
                .isInstanceOf(ConstraintViolationException.class);
    }

    // ---------- UTILISATEURS ----------

    @Test
    void save_shouldAssociateUsersCorrectly() {
        User u1 = userRepository.save(
                new User().setEmail("u1@test.com").setFirstName("U1").setLastName("Test").setPassword("password")
        );
        User u2 = userRepository.save(
                new User().setEmail("u2@test.com").setFirstName("U2").setLastName("Test").setPassword("password")
        );

        Session session = Session.builder()
                .name("Session With Users")
                .description("Description")
                .date(new Date())
                .teacher(savedTeacher)
                .users(new ArrayList<>(List.of(u1, u2)))
                .build();

        Session saved = sessionRepository.save(session);

        assertThat(saved.getUsers()).containsExactlyInAnyOrder(u1, u2);
    }
}
