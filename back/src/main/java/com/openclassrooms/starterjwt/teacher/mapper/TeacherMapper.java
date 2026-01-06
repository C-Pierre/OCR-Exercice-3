package com.openclassrooms.starterjwt.teacher.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.teacher.dto.TeacherDto;
import com.openclassrooms.starterjwt.common.mapper.EntityMapper;

@Component
@Mapper(componentModel = "spring")
public interface TeacherMapper extends EntityMapper<TeacherDto, Teacher> {
}
