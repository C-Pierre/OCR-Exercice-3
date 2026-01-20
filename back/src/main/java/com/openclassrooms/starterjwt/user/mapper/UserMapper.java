package com.openclassrooms.starterjwt.user.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.user.dto.UserDto;
import com.openclassrooms.starterjwt.common.mapper.EntityMapper;

@Component
@Mapper(componentModel = "spring")
public abstract class UserMapper implements EntityMapper<UserDto, User> {

    @Override
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAdmin(user.isAdmin());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        // On n’expose jamais le password
        return dto;
    }

    @Override
    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAdmin(dto.isAdmin());
        user.setCreatedAt(dto.getCreatedAt());
        user.setUpdatedAt(dto.getUpdatedAt());

        // Password non mappé depuis DTO
        user.setPassword(null);

        return user;
    }

    @Override
    public List<UserDto> toDto(List<User> users) {
        if (users == null) return Collections.emptyList();
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<User> toEntity(List<UserDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
