package com.openclassrooms.starterjwt.session.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.openclassrooms.starterjwt.teacher.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.session.model.Session;
import com.openclassrooms.starterjwt.user.mapper.UserMapper;
import com.openclassrooms.starterjwt.session.dto.SessionDto;
import com.openclassrooms.starterjwt.common.mapper.EntityMapper;

@Component
@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class},
    imports = {Arrays.class, Collectors.class, Session.class, User.class, Collections.class, Optional.class}
)
public abstract class SessionMapper implements EntityMapper<SessionDto, Session> {

    @Autowired
    protected UserMapper userMapper; // <-- obligatoire pour l'expression

    @Mapping(source = "description", target = "description")
    @Mapping(source = "teacher.id", target = "teacherId")
    @Mapping(
            target = "users",
            expression = "java(Optional.ofNullable(session.getUsers())"
                    + ".orElseGet(Collections::emptyList)"
                    + ".stream()"
                    + ".map(userMapper::toDto)"
                    + ".collect(Collectors.toList()))"
    )
    public abstract SessionDto toDto(Session session);

    @Override
    public Session toEntity(SessionDto dto) {
        if (dto == null) return null;

        Session session = new Session();
        session.setId(dto.getId());
        session.setName(dto.getName());
        session.setDescription(dto.getDescription());

        // Mapper teacher uniquement si teacherId != null
        if (dto.getTeacherId() != null) {
            Teacher teacher = new Teacher();
            teacher.setId(dto.getTeacherId());
            session.setTeacher(teacher);
        }

        // Users → on laisse vide, car les DTOs utilisateurs ne sont pas mappés ici
        session.setUsers(Collections.emptyList());

        return session;
    }

    @Override
    public List<Session> toEntity(List<SessionDto> dtoList) {
        if (dtoList == null) return Collections.emptyList();
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<SessionDto> toDto(List<Session> entityList) {
        if (entityList == null) return Collections.emptyList();
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
