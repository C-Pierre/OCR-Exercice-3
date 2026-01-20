package com.openclassrooms.starterjwt.session.repository.port;

import java.util.List;
import java.util.Optional;
import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.common.exception.NotFoundException;

public interface SessionRepositoryPort {

    Session getById(Long id) throws NotFoundException;

    Session getByNameAndTeacherId(String name, Long teacherId) throws NotFoundException;

    void save(Session session);

    void delete(Session session);

    List<Session> findAll();
}