package com.openclassrooms.starterjwt.session.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.session.repository.port.SessionRepositoryPort;

@Service
public class DeleteSessionService {

    private final SessionRepositoryPort sessionRepositoryPort;

    public DeleteSessionService(
        SessionRepositoryPort sessionRepositoryPort
    ) {
        this.sessionRepositoryPort = sessionRepositoryPort;
    }

    public void execute(Long id) {
        Session session = sessionRepositoryPort.getById(id);
        sessionRepositoryPort.delete(session);
    }
}
