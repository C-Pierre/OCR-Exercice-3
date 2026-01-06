package com.openclassrooms.starterjwt.teacher.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.teacher.dto.TeacherDto;
import com.openclassrooms.starterjwt.teacher.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.teacher.repository.port.TeacherRepositoryPort;

@Service
public class GetTeacherService {

    private final TeacherMapper teacherMapper;
    private final TeacherRepositoryPort teacherRepositoryPort;

    public GetTeacherService(
        TeacherMapper teacherMapper,
        TeacherRepositoryPort teacherRepositoryPort
    ) {
        this.teacherMapper = teacherMapper;
        this.teacherRepositoryPort = teacherRepositoryPort;
    }

    public TeacherDto execute(Long id) {
        return teacherMapper.toDto(teacherRepositoryPort.getById(id));
    }
}
