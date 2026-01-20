package com.openclassrooms.starterjwt.user.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.starterjwt.user.dto.UserDto;
import com.openclassrooms.starterjwt.user.mapper.UserMapper;
import com.openclassrooms.starterjwt.user.repository.port.UserRepositoryPort;

@Service
public class GetUserService {

    private final UserMapper userMapper;
    private final UserRepositoryPort userRepositoryPort;

    public GetUserService(
        UserMapper userMapper,
        UserRepositoryPort userRepositoryPort
    ) {
        this.userMapper = userMapper;
        this.userRepositoryPort = userRepositoryPort;
    }

    public UserDto execute(Long id) {
        return userMapper.toDto(userRepositoryPort.getById(id));
    }
}
