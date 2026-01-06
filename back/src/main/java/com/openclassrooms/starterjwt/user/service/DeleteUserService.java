package com.openclassrooms.starterjwt.user.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.common.exception.UnauthorizedException;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;

@Service
public class DeleteUserService {

    private final UserRepositoryPort userRepositoryPort;

    public DeleteUserService(
        UserRepositoryPort userRepositoryPort
    ) {
        this.userRepositoryPort = userRepositoryPort;
    }

    public void execute(Long id, String authenticatedEmail) {
        User user = userRepositoryPort.getById(id);

        if (!user.getEmail().equals(authenticatedEmail)) {
            throw new UnauthorizedException("You are not allowed to delete this user");
        }

        userRepositoryPort.delete(user);
    }
}
