package com.openclassrooms.starterjwt.user.repository.port;

import java.util.List;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.common.exception.NotFoundException;

public interface UserRepositoryPort {

    User getById(Long id) throws NotFoundException;

    User getByEmail(String email) throws NotFoundException;

    Boolean existsByEmail(String email);

    void save(User user);

    void delete(User user);

    List<User> findAll();

    List<User> findAllById(List<Long> ids);
}