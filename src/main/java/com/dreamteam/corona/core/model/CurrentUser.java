
package com.dreamteam.corona.core.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(User user, Collection<? extends GrantedAuthority> auths) {
        super(user.getUsername(),
                user.getPassword(),
                auths);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
