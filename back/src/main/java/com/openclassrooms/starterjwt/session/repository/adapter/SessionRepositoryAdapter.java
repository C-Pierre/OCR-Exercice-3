package com.openclassrooms.starterjwt.session.repository.adapter;

import java.util.List;
import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.common.exception.NotFoundException;
import com.openclassrooms.starterjwt.session.repository.SessionRepository;
import com.openclassrooms.starterjwt.session.repository.port.SessionRepositoryPort;

@Service
public class SessionRepositoryAdapter implements SessionRepositoryPort {

    private final SessionRepository sessionRepository;

    public SessionRepositoryAdapter(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Session getById(Long id) {
        return sessionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Session not found with id: " + id));
    }

    @Override
    public Session getByNameAndTeacherId(String name, Long teacherId) {
        return sessionRepository.findByNameAndTeacherId(name, teacherId)
            .orElseThrow(
                () -> new NotFoundException("Session not found with name: " + name + " and teacher_id: " + teacherId)
            );
    }

    @Override
    public void save(Session session) {
        sessionRepository.save(session);
    }

    @Override
    public void delete(Session session) {
        sessionRepository.delete(session);
    }

    @Override
    public List<Session> findAll() {
        return sessionRepository.findAll();
    }
}