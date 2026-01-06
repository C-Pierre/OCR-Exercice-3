package com.openclassrooms.starterjwt.session.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.session.dto.SessionDto;
import com.openclassrooms.starterjwt.session.mapper.SessionMapper;
import com.openclassrooms.starterjwt.session.repository.port.SessionRepositoryPort;

@Service
public class GetSessionsService {

    private final SessionMapper sessionMapper;
    private final SessionRepositoryPort sessionRepositoryPort;

    public GetSessionsService(
        SessionMapper sessionMapper,
        SessionRepositoryPort sessionRepositoryPort
    ) {
        this.sessionMapper = sessionMapper;
        this.sessionRepositoryPort = sessionRepositoryPort;
    }

    public List<SessionDto> execute() {
        return sessionRepositoryPort.findAll()
            .stream()
            .map(sessionMapper::toDto)
            .toList();
    }
}
