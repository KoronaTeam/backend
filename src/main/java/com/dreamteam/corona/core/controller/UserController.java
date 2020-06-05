package com.dreamteam.corona.core.controller;

import com.dreamteam.corona.core.dto.UserDto;
import com.dreamteam.corona.core.mapper.UserMapper;
import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.core.repository.UserRepository;
import com.dreamteam.corona.core.service.UserService;
import com.dreamteam.corona.quarantine.model.Ticket;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping("/users/{id}")
    UserDto getOneUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDto userDto = userMapper.userToDto(user);

        return userDto;
    }

    @GetMapping("/users")
    List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.flatUsersToDtos(users);
    }

    @PostMapping("/users")
    UserDto saveNewUser(@RequestBody UserDto userDto) {
        User user = userMapper.dtoToUser(userDto);
        user = userService.createUserSendConfirmation(user);

        return userMapper.userToDto(user);
    }
}
