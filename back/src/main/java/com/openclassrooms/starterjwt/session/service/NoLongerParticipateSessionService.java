package com.openclassrooms.starterjwt.session.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.common.exception.BadRequestException;
import com.openclassrooms.starterjwt.session.repository.port.SessionRepositoryPort;

@Service
public class NoLongerParticipateSessionService {

    private final SessionRepositoryPort sessionRepositoryPort;

    public NoLongerParticipateSessionService(
        SessionRepositoryPort sessionRepositoryPort
    ) {
        this.sessionRepositoryPort = sessionRepositoryPort;
    }

    public void execute(Long sessionId, Long userId) {
        Session session = sessionRepositoryPort.getById(sessionId);

        boolean removed = session.getUsers().removeIf(u -> u.getId().equals(userId));
        if (!removed) {
            throw new BadRequestException("User does not participate in this session");
        }

        sessionRepositoryPort.save(session);
    }
}
