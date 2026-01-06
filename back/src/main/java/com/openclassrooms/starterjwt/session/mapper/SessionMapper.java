package com.openclassrooms.starterjwt.session.mapper;

import java.util.Arrays;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.Collections;
import java.util.stream.Collectors;
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

    @Mapping(source = "description", target = "description")
    @Mapping(source = "teacher.id", target = "teacherId")
    @Mapping(
        target = "users",
        expression = "java(Optional.ofNullable(session.getUsers())" +
            ".orElseGet(Collections::emptyList)" +
            ".stream()" +
            ".map(userMapper::toDto)" +
            ".collect(Collectors.toList()))"
    )
    public abstract SessionDto toDto(Session session);
}
