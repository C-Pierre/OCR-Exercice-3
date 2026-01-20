package com.openclassrooms.starterjwt.session.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.common.exception.BadRequestException;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;
import com.openclassrooms.starterjwt.session.repository.port.SessionRepositoryPort;

@Service
public class ParticipateSessionService {

    private final SessionRepositoryPort sessionRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    public ParticipateSessionService(
        SessionRepositoryPort sessionRepositoryPort,
        UserRepositoryPort userRepositoryPort
    ) {
        this.sessionRepositoryPort = sessionRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
    }

    public void execute(Long sessionId, Long userId) {
        Session session = sessionRepositoryPort.getById(sessionId);
        User user = userRepositoryPort.getById(userId);

        if (session.getUsers().stream().anyMatch(u -> u.getId().equals(userId))) {
            throw new BadRequestException("User already participates in this session");
        }

        session.getUsers().add(user);
        sessionRepositoryPort.save(session);
    }
}
