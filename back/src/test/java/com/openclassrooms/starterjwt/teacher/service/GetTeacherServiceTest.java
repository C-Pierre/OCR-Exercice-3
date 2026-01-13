package com.openclassrooms.starterjwt.teacher.service;

import com.openclassrooms.starterjwt.teacher.dto.TeacherDto;
import com.openclassrooms.starterjwt.teacher.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.teacher.repository.port.TeacherRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class GetTeacherServiceTest {

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private TeacherRepositoryPort teacherRepositoryPort;

    @InjectMocks
    private GetTeacherService getTeacherService;

    @Test
    void should_return_teacher_dto_when_teacher_exists() {
        // GIVEN
        Long teacherId = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);

        TeacherDto teacherDto = new TeacherDto();

        when(teacherRepositoryPort.getById(teacherId)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        // WHEN
        TeacherDto result = getTeacherService.execute(teacherId);

        // THEN
        assertThat(result).isEqualTo(teacherDto);
        verify(teacherRepositoryPort).getById(teacherId);
        verify(teacherMapper).toDto(teacher);
    }

    @Test
    void should_throw_exception_when_teacher_not_found() {
        // GIVEN
        Long teacherId = 99L;
        when(teacherRepositoryPort.getById(teacherId))
                .thenThrow(new RuntimeException("Teacher not found"));

        // WHEN & THEN
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> getTeacherService.execute(teacherId));

        verify(teacherRepositoryPort).getById(teacherId);
        verifyNoInteractions(teacherMapper);
    }
}
