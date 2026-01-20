package com.openclassrooms.starterjwt.session.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.session.dto.SessionDto;
import com.openclassrooms.starterjwt.session.mapper.SessionMapper;
import com.openclassrooms.starterjwt.session.repository.port.SessionRepositoryPort;

@Service
public class GetSessionService {

    private final SessionMapper sessionMapper;
    private final SessionRepositoryPort sessionRepositoryPort;

    public GetSessionService(
        SessionMapper sessionMapper,
        SessionRepositoryPort sessionRepositoryPort
    ) {
        this.sessionMapper = sessionMapper;
        this.sessionRepositoryPort = sessionRepositoryPort;
    }

    public SessionDto execute(Long id) {
        return sessionMapper.toDto(sessionRepositoryPort.getById(id));
    }
}
