package com.openclassrooms.starterjwt.teacher.repository.adapter;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.common.exception.NotFoundException;
import com.openclassrooms.starterjwt.teacher.repository.TeacherRepository;
import com.openclassrooms.starterjwt.teacher.repository.port.TeacherRepositoryPort;

@Service
public class TeacherRepositoryAdapter implements TeacherRepositoryPort {

    private final TeacherRepository teacherRepository;

    public TeacherRepositoryAdapter(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Teacher getById(Long id) {
        return teacherRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    @Override
    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }
}