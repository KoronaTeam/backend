package com.dreamteam.corona.core.security;

import com.dreamteam.corona.core.model.CurrentUser;
import com.dreamteam.corona.core.model.Privilege;
import com.dreamteam.corona.core.model.Role;
import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedHashSet;

@Service("currentUserDetailsService")
public class CurrentUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CurrentUserDetailsService.class);

    private final UserService userService;

    static final String USER_IS_DISABLED = "User is disabled";

    public CurrentUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info("Load user: {}", username);

        User user = userService.getUserByUsername(username);
        if (!user.isEnabled()) {
            throw new DisabledException(USER_IS_DISABLED);
        }
        logger.info("User roles: {}", user.getRoles());
        logger.info("User pivileges {}", getAuthFromPrivileges(user.getRoles()));
        return new CurrentUser(user, getAuthFromPrivileges(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthFromPrivileges(
            Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private LinkedHashSet<String> getPrivileges(Collection<Role> roles) {

        LinkedHashSet<String> privileges = new LinkedHashSet<>();
        LinkedHashSet<Privilege> collection = new LinkedHashSet<>();
        for (Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private LinkedHashSet<GrantedAuthority> getGrantedAuthorities(LinkedHashSet<String> privileges) {
        LinkedHashSet<GrantedAuthority> auths = new LinkedHashSet<>();
        for (String privilege : privileges) {
            auths.add(new SimpleGrantedAuthority(privilege));
        }
        return auths;
    }

}
