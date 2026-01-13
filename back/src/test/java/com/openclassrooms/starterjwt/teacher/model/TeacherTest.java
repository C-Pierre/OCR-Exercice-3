package com.openclassrooms.starterjwt.teacher.model;

import com.openclassrooms.starterjwt.teacher.dto.TeacherDto;
import com.openclassrooms.starterjwt.teacher.mapper.TeacherMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.Set;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TeacherTest {

    @Autowired
    private TeacherMapper teacherMapper;

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validTeacher_shouldHaveNoViolations() {
        Teacher teacher = Teacher.builder()
                .firstName("Ada")
                .lastName("Lovelace")
                .build();

        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertThat(violations).isEmpty();
    }

    @Test
    void firstNameAndLastName_cannotBeBlank() {
        Teacher t1 = new Teacher().setFirstName("").setLastName("Last");
        Teacher t2 = new Teacher().setFirstName("First").setLastName("");

        assertThat(validator.validate(t1))
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
        assertThat(validator.validate(t2))
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }

    @Test
    void equals_shouldBehaveCorrectly() {
        Teacher t1 = Teacher.builder().id(1L).firstName("Ada").build();
        Teacher t2 = Teacher.builder().id(1L).firstName("Marie").build();
        Teacher t3 = Teacher.builder().id(2L).firstName("Ada").build();
        Teacher tNull = Teacher.builder().firstName("Null").build(); // id null

        assertThat(t1).isEqualTo(t1);

        assertThat(t1).isEqualTo(t2);

        assertThat(t1).isNotEqualTo(t3);

        assertThat(tNull).isNotEqualTo(t1);
        assertThat(tNull).isEqualTo(tNull);

        assertThat(t1).isNotEqualTo(null);
        assertThat(t1).isNotEqualTo("string");

        assertThat(t1.canEqual(t2)).isTrue();
        assertThat(t1.canEqual("string")).isFalse();
    }

    @Test
    void hashCode_shouldBehaveCorrectly() {
        Teacher t1 = Teacher.builder().id(1L).build();
        Teacher t2 = Teacher.builder().id(1L).build();
        Teacher t3 = Teacher.builder().id(2L).build();
        Teacher tNull = Teacher.builder().build(); // id null

        assertThat(t1.hashCode()).isEqualTo(t2.hashCode());
        assertThat(t1.hashCode()).isNotEqualTo(t3.hashCode());

        // id null → hashCode does not throw
        assertThatCode(tNull::hashCode).doesNotThrowAnyException();
    }

    @Test
    void builder_andSetters_shouldWorkWithCreatedAtUpdatedAt() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                .id(10L)
                .firstName("Alan")
                .lastName("Turing")
                .build()
                .setCreatedAt(now)
                .setUpdatedAt(now);

        assertThat(teacher.getId()).isEqualTo(10L);
        assertThat(teacher.getFirstName()).isEqualTo("Alan");
        assertThat(teacher.getLastName()).isEqualTo("Turing");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void toString_shouldIncludeAllFieldsAndDates() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                .id(99L)
                .firstName("Grace")
                .lastName("Hopper")
                .build()
                .setCreatedAt(now)
                .setUpdatedAt(now);

        String str = teacher.toString();

        assertThat(str).contains("99");
        assertThat(str).contains("Grace");
        assertThat(str).contains("Hopper");
        assertThat(str).contains(now.toString());
    }

    @Test
    void teacherMapper_toDtoAndToEntity_shouldMapCorrectly() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("Ada")
                .lastName("Lovelace")
                .build();

        TeacherDto dto = teacherMapper.toDto(teacher);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(teacher.getId());
        assertThat(dto.getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(teacher.getLastName());

        Teacher entity = teacherMapper.toEntity(dto);
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(entity.getLastName()).isEqualTo(dto.getLastName());
    }

    @Test
    void builder_shouldSetCreatedAtAndUpdatedAt_andToString() {
        LocalDateTime now = LocalDateTime.now();

        Teacher teacher = Teacher.builder()
                .id(10L)
                .firstName("Alan")
                .lastName("Turing")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertThat(teacher.getId()).isEqualTo(10L);
        assertThat(teacher.getFirstName()).isEqualTo("Alan");
        assertThat(teacher.getLastName()).isEqualTo("Turing");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);

        String str = teacher.toString();
        assertThat(str).contains("Alan");
        assertThat(str).contains("Turing");
        assertThat(str).contains("10");
        assertThat(str).contains(now.toString());
    }
}
