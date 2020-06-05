package com.dreamteam.corona.core.mapper;

import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.core.dto.UserDto;
import com.dreamteam.corona.quarantine.mapper.QuarantineMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {QuarantineMapper.class})
public interface UserMapper {

    // ToDo: do not have authorities mapping, but I'am not sure I need it
    @Named("fullUserToDto")
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "quarantine", qualifiedByName = "quarantineToFlatDto")
    UserDto userToDto(User user);

    @Named("fullDtoToUser")
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "quarantine", ignore = true)
    User dtoToUser(UserDto userDto);

    @Named("flatUserToDto")
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "quarantine", ignore = true)
    UserDto userToFlatDto(User user);

    @Named("superFlatUserToDto")
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "quarantine", ignore = true)
    UserDto superFlatUserToDto(User user);

    @Named("flatUsersToDtos")
    @IterableMapping(qualifiedByName = "fullUserToDto")
    List<UserDto> flatUsersToDtos(List<User> users);

}
