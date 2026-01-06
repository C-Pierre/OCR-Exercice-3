package com.openclassrooms.starterjwt.user.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import com.openclassrooms.starterjwt.user.model.User;
import com.openclassrooms.starterjwt.user.dto.UserDto;
import com.openclassrooms.starterjwt.common.mapper.EntityMapper;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, User> {
}
