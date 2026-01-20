package com.openclassrooms.starterjwt.teacher.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.teacher.dto.TeacherDto;
import com.openclassrooms.starterjwt.common.mapper.EntityMapper;

@Component
@Mapper(componentModel = "spring")
public abstract class TeacherMapper implements EntityMapper<TeacherDto, Teacher> {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    public abstract TeacherDto toDto(Teacher teacher);

    @Override
    public Teacher toEntity(TeacherDto dto) {
        if (dto == null) return null;

        Teacher teacher = new Teacher();
        teacher.setId(dto.getId());
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());

        return teacher;
    }

    @Override
    public List<Teacher> toEntity(List<TeacherDto> dtoList) {
        if (dtoList == null) return Collections.emptyList();
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<TeacherDto> toDto(List<Teacher> entityList) {
        if (entityList == null) return Collections.emptyList();
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
