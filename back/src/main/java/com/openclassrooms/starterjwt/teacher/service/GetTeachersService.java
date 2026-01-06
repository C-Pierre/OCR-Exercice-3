package com.openclassrooms.starterjwt.teacher.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.teacher.dto.TeacherDto;
import com.openclassrooms.starterjwt.teacher.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.teacher.repository.port.TeacherRepositoryPort;

@Service
public class GetTeachersService {

    private final TeacherMapper teacherMapper;
    private final TeacherRepositoryPort teacherRepositoryPort;

    public GetTeachersService(
            TeacherMapper teacherMapper,
        TeacherRepositoryPort teacherRepositoryPort
    ) {
        this.teacherMapper = teacherMapper;
        this.teacherRepositoryPort = teacherRepositoryPort;
    }

    public List<TeacherDto> execute() {
        return teacherRepositoryPort.findAll()
            .stream()
            .map(teacherMapper::toDto)
            .toList();
    }
}
