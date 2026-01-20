package com.openclassrooms.starterjwt.teacher.repository;

import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolationException;
import static org.assertj.core.api.Assertions.assertThat;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TeacherRepositoryIntegrationTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void save_shouldPopulateAuditFields() {
        Teacher teacher = Teacher.builder()
                .firstName("Ada")
                .lastName("Lovelace")
                .build();

        Teacher saved = teacherRepository.save(teacher);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void save_shouldFailWhenFirstNameIsBlank() {
        Teacher teacher = Teacher.builder()
                .firstName("")
                .lastName("Doe")
                .build();

        assertThatThrownBy(() -> teacherRepository.saveAndFlush(teacher))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void save_shouldFailWhenLastNameIsBlank() {
        Teacher teacher = Teacher.builder()
                .firstName("John")
                .lastName("")
                .build();

        assertThatThrownBy(() -> teacherRepository.saveAndFlush(teacher))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void save_shouldFailWhenFirstNameExceedsMaxLength() {
        Teacher teacher = Teacher.builder()
                .firstName("A".repeat(21))
                .lastName("Doe")
                .build();

        assertThatThrownBy(() -> teacherRepository.saveAndFlush(teacher))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void save_shouldFailWhenLastNameExceedsMaxLength() {
        Teacher teacher = Teacher.builder()
                .firstName("John")
                .lastName("B".repeat(21))
                .build();

        assertThatThrownBy(() -> teacherRepository.saveAndFlush(teacher))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
