package com.dreamteam.corona.core.service;

import com.dreamteam.corona.core.model.User;

public interface UserService {

    User getUserByUsername(String username);
    User getUserById(Long id);
    boolean canAccessUser(String username);
//    User createUnregisteredUser(User user);
//    void sendRegistrationEmail(User user);
//    void sendTestEmail();
    User createUserSendConfirmation(User user);
}
