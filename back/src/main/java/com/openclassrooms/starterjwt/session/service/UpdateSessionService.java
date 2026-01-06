package com.openclassrooms.starterjwt.session.service;

import java.util.List;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.session.dto.SessionDto;
import com.openclassrooms.starterjwt.session.mapper.SessionMapper;
import com.openclassrooms.starterjwt.session.request.UpdateSessionRequest;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;
import com.openclassrooms.starterjwt.teacher.repository.port.TeacherRepositoryPort;
import com.openclassrooms.starterjwt.session.repository.port.SessionRepositoryPort;

@Service
public class UpdateSessionService {

    private final SessionMapper sessionMapper;
    private final SessionRepositoryPort sessionRepositoryPort;
    private final TeacherRepositoryPort teacherRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    public UpdateSessionService(
        SessionMapper sessionMapper,
        SessionRepositoryPort sessionRepositoryPort,
        TeacherRepositoryPort teacherRepositoryPort,
        UserRepositoryPort userRepositoryPort
    ) {
        this.sessionMapper = sessionMapper;
        this.sessionRepositoryPort = sessionRepositoryPort;
        this.teacherRepositoryPort = teacherRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
    }

    public SessionDto execute(Long id, UpdateSessionRequest request) {
        Session session = sessionRepositoryPort.getById(id);
        boolean modified = false;

        if (request.getName() != null && !request.getName().equals(session.getName())) {
            session.setName(request.getName());
            modified = true;
        }

        if (request.getDate() != null && !request.getDate().equals(session.getDate())) {
            session.setDate(request.getDate());
            modified = true;
        }

        if (request.getDescription() != null && !request.getDescription().equals(session.getDescription())) {
            session.setDescription(request.getDescription());
            modified = true;
        }

        if (
            request.getTeacherId() != null &&
            (session.getTeacher() == null || !request.getTeacherId().equals(session.getTeacher().getId()))
        ) {
            Teacher teacher = teacherRepositoryPort.getById(request.getTeacherId());
            session.setTeacher(teacher);
            modified = true;
        }

        if (request.getUsers() != null) {
            List<User> newUsers = userRepositoryPort.findAllById(request.getUsers());

            if (!newUsers.equals(session.getUsers())) {
                session.setUsers(newUsers);
                modified = true;
            }
        }

        if (modified) {
            session.setUpdatedAt(LocalDateTime.now());
            sessionRepositoryPort.save(session);
        }

        return sessionMapper.toDto(session);
    }
}
