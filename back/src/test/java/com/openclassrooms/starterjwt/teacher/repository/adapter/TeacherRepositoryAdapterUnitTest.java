package com.openclassrooms.starterjwt.teacher.repository.adapter;

import com.openclassrooms.starterjwt.common.exception.NotFoundException;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.teacher.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class TeacherRepositoryAdapterUnitTest {

    private TeacherRepository teacherRepository;
    private TeacherRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        teacherRepository = mock(TeacherRepository.class);
        adapter = new TeacherRepositoryAdapter(teacherRepository);
    }

    @Test
    void getById_shouldReturnTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Teacher result = adapter.getById(1L);
        assertThat(result).isEqualTo(teacher);
    }

    @Test
    void getById_shouldThrowNotFound() {
        when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adapter.getById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void findAll_shouldReturnAllTeachers() {
        List<Teacher> list = List.of(new Teacher(), new Teacher());
        when(teacherRepository.findAll()).thenReturn(list);

        List<Teacher> result = adapter.findAll();
        assertThat(result).hasSize(2);
    }

    @Test
    void getByLastName_shouldReturnTeacher_mock() {
        Teacher teacher = new Teacher();
        teacher.setLastName("Hopper");
        when(teacherRepository.findByLastName("Hopper")).thenReturn(Optional.of(teacher));

        Teacher result = adapter.getByLastName("Hopper");
        assertThat(result).isEqualTo(teacher);
    }

    @Test
    void getByLastName_shouldThrowNotFound_mock() {
        when(teacherRepository.findByLastName("Nonexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adapter.getByLastName("Nonexistent"))
                .isInstanceOf(NotFoundException.class);
    }
}
