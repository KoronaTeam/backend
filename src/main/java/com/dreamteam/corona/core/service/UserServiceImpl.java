package com.dreamteam.corona.core.service;

import com.dreamteam.corona.core.exception.UserNotFoundException;
import com.dreamteam.corona.core.model.CurrentUser;
import com.dreamteam.corona.core.model.Role;
import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.core.mapper.UserMapper;
import com.dreamteam.corona.core.repository.RoleRepository;
import com.dreamteam.corona.core.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Transactional
@Service("userService")
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private UserMapper userMapper;
    private RoleRepository roleRepository;
    private EmailService emailService;

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Nie odnaleziono użytkownika o id: " + id));
    }

    @Override
    public boolean canAccessUser(String username) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

        logger.info("Checking if user={} has access to user={}", currentUser.getUsername(), username);
        if (currentUser != null
                && (currentUser.getUser().hasRole("ROLE_ADMIN")
                || currentUser.getUsername().equals(username)) )  {
            logger.info("User {} has enough privileges", username);
            return true;
        } else {
            logger.info("User {} has not privileges to see content", currentUser.getUsername());
            return false;
        }
    }

//    @Override
//    public User createUnregisteredUser(User user) {
//        user = userRepository.save(user);
//        sendRegistrationEmail(user);
//        return user;
//    }

    @Override
    public User createUserSendConfirmation(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = user.getPassword();

        Role role = roleRepository.findByName("ROLE_USER");
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Arrays.asList(role));
        user.setEnabled(true);

        user = userRepository.save(user);
        emailService.sendRegistrationConfirmation(user, password);
        return user;
    }


//    @Override
//    public void sendRegistrationEmail(User user) {
//
//        emailService.prepareAndSend(user, "TOKEN$%^&GH%^&*GHUN");
//    }
//
//    @Override
//    public void sendTestEmail() {
//
//        User user = new User("dreamteam.corona", "12345", "Dominik", "Kuzaka", "dkuzaka@gmail.com");
//        emailService.prepareAndSend(user, "TOKEN$%^&GH%^&*GHUN");
//    }

}
