package com.openclassrooms.starterjwt.session.service;

import java.util.List;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.teacher.model.Teacher;
import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.session.dto.SessionDto;
import com.openclassrooms.starterjwt.session.mapper.SessionMapper;
import com.openclassrooms.starterjwt.session.request.CreateSessionRequest;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;
import com.openclassrooms.starterjwt.teacher.repository.port.TeacherRepositoryPort;
import com.openclassrooms.starterjwt.session.repository.port.SessionRepositoryPort;

@Service
public class CreateSessionService {

    private final SessionMapper sessionMapper;
    private final SessionRepositoryPort sessionRepositoryPort;
    private final TeacherRepositoryPort teacherRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    public CreateSessionService(
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

    public SessionDto execute(CreateSessionRequest request) {
        Teacher teacher = teacherRepositoryPort.getById(request.getTeacherId());
        List<User> newUsers = null;

        if (request.getUsers() != null) {
            newUsers = userRepositoryPort.findAllById(request.getUsers());
        }

        Session session = Session.builder()
            .name(request.getName())
            .date(request.getDate())
            .description(request.getDescription())
            .teacher(teacher)
            .users(newUsers)
            .createdAt(LocalDateTime.now())
            .build();

        sessionRepositoryPort.save(session);

        return sessionMapper.toDto(session);
    }
}
