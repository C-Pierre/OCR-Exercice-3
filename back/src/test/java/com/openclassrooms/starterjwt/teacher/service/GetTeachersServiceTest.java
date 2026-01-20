package com.openclassrooms.starterjwt.teacher.service;

import com.openclassrooms.starterjwt.teacher.dto.TeacherDto;
import com.openclassrooms.starterjwt.teacher.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.teacher.repository.port.TeacherRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class GetTeachersServiceTest {

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private TeacherRepositoryPort teacherRepositoryPort;

    @InjectMocks
    private GetTeachersService getTeachersService;

    @Test
    void should_return_list_of_teacher_dtos() {
        // GIVEN
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);

        TeacherDto dto1 = new TeacherDto();
        TeacherDto dto2 = new TeacherDto();

        when(teacherRepositoryPort.findAll()).thenReturn(List.of(teacher1, teacher2));
        when(teacherMapper.toDto(teacher1)).thenReturn(dto1);
        when(teacherMapper.toDto(teacher2)).thenReturn(dto2);

        // WHEN
        List<TeacherDto> result = getTeachersService.execute();

        // THEN
        assertThat(result).containsExactly(dto1, dto2);
        verify(teacherRepositoryPort).findAll();
        verify(teacherMapper).toDto(teacher1);
        verify(teacherMapper).toDto(teacher2);
    }

    @Test
    void should_return_empty_list_when_no_teachers() {
        // GIVEN
        when(teacherRepositoryPort.findAll()).thenReturn(List.of());

        // WHEN
        List<TeacherDto> result = getTeachersService.execute();

        // THEN
        assertThat(result).isEmpty();
        verify(teacherRepositoryPort).findAll();
        verifyNoInteractions(teacherMapper);
    }
}
