package com.openclassrooms.starterjwt.teacher.repository.adapter;

import com.openclassrooms.starterjwt.common.exception.NotFoundException;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.teacher.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class TeacherRepositoryAdapterTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void saveAndFindAll_shouldWork() {
        Teacher t1 = new Teacher();
        t1.setFirstName("Ada");
        t1.setLastName("Lovelace");

        Teacher t2 = new Teacher();
        t2.setFirstName("Alan");
        t2.setLastName("Turing");

        teacherRepository.save(t1);
        teacherRepository.save(t2);

        List<Teacher> teachers = teacherRepository.findAll();
        assertThat(teachers).hasSize(2);
    }

    @Test
    void getById_shouldReturnSavedTeacher() {
        Teacher t = new Teacher();
        t.setFirstName("Grace");
        t.setLastName("Hopper");
        t = teacherRepository.save(t);

        Teacher found = teacherRepository.findById(t.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getFirstName()).isEqualTo("Grace");
        assertThat(found.getLastName()).isEqualTo("Hopper");
    }

    @Test
    void getByLastName_shouldReturnTeacher() {
        Teacher t = new Teacher();
        t.setFirstName("Ada");
        t.setLastName("Lovelace");
        t = teacherRepository.save(t);

        Teacher found = new TeacherRepositoryAdapter(teacherRepository)
                .getByLastName("Lovelace");

        assertThat(found).isNotNull();
        assertThat(found.getFirstName()).isEqualTo("Ada");
        assertThat(found.getLastName()).isEqualTo("Lovelace");
    }

    @Test
    void getByLastName_shouldThrowNotFoundException() {
        TeacherRepositoryAdapter adapter = new TeacherRepositoryAdapter(teacherRepository);

        assertThatThrownBy(() -> adapter.getByLastName("Nonexistent"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User not found with lastname");
    }
}