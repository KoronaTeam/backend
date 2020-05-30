package com.dreamteam.corona.core.mapper;

import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.core.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // ToDo: do not have authorities mapping, but I'am not sure I need it
    @Named("fullUserToDto")
    @Mapping(target = "authorities", ignore = true)
    UserDto userToDto(User user);

    @Named("fullDtoToUser")
    @Mapping(target = "authorities", ignore = true)
    User dtoToUser(UserDto userDto);
}
