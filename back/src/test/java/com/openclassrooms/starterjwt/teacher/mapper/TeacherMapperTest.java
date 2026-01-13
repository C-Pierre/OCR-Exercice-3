package com.openclassrooms.starterjwt.teacher.mapper;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.teacher.dto.TeacherDto;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SpringBootTest
class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    // ---------- ENTITY → DTO ----------

    @Test
    void shouldMapEntityToDto() {
        Teacher teacher = new Teacher();
        teacher.setId(5L);
        teacher.setFirstName("Ada");
        teacher.setLastName("Lovelace");

        TeacherDto dto = teacherMapper.toDto(teacher);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getFirstName()).isEqualTo("Ada");
        assertThat(dto.getLastName()).isEqualTo("Lovelace");
    }

    @Test
    void shouldReturnNullDtoWhenEntityIsNull() {
        TeacherDto dto = teacherMapper.toDto((Teacher) null);
        assertThat(dto).isNull();
    }

    @Test
    void shouldMapTeacherListToDtoList() {
        Teacher t1 = new Teacher();
        t1.setId(1L);
        Teacher t2 = new Teacher();
        t2.setId(2L);

        List<Teacher> teachers = List.of(t1, t2);

        List<TeacherDto> dtos = teacherMapper.toDto(teachers);

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getId()).isEqualTo(1L);
        assertThat(dtos.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void shouldReturnEmptyDtoListWhenEntityListIsNull() {
        List<TeacherDto> dtos = teacherMapper.toDto((List<Teacher>) null);
        assertThat(dtos).isNotNull().isEmpty();
    }

    // ---------- DTO → ENTITY ----------

    @Test
    void shouldMapDtoToEntity() {
        TeacherDto dto = new TeacherDto();
        dto.setId(8L);
        dto.setFirstName("Alan");
        dto.setLastName("Turing");

        Teacher teacher = teacherMapper.toEntity(dto);

        assertThat(teacher).isNotNull();
        assertThat(teacher.getId()).isEqualTo(8L);
        assertThat(teacher.getFirstName()).isEqualTo("Alan");
        assertThat(teacher.getLastName()).isEqualTo("Turing");
    }

    @Test
    void shouldReturnNullEntityWhenDtoIsNull() {
        Teacher teacher = teacherMapper.toEntity((TeacherDto) null);
        assertThat(teacher).isNull();
    }

    @Test
    void shouldMapDtoListToTeacherList() {
        TeacherDto d1 = new TeacherDto();
        d1.setId(10L);
        TeacherDto d2 = new TeacherDto();
        d2.setId(20L);

        List<TeacherDto> dtos = List.of(d1, d2);

        List<Teacher> teachers = teacherMapper.toEntity(dtos);

        assertThat(teachers).hasSize(2);
        assertThat(teachers.get(0).getId()).isEqualTo(10L);
        assertThat(teachers.get(1).getId()).isEqualTo(20L);
    }

    @Test
    void shouldReturnEmptyEntityListWhenDtoListIsNull() {
        List<Teacher> teachers = teacherMapper.toEntity((List<TeacherDto>) null);
        assertThat(teachers).isNotNull().isEmpty();
    }
}
