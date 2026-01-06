package com.openclassrooms.starterjwt.teacher.repository.port;

import java.util.List;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.common.exception.NotFoundException;

public interface TeacherRepositoryPort {

    Teacher getById(Long id) throws NotFoundException;

    List<Teacher> findAll();
}