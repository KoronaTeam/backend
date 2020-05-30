package com.dreamteam.corona.core.configuration;

import com.dreamteam.corona.core.model.Privilege;
import com.dreamteam.corona.core.model.Role;
import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.core.repository.PrivilegeRepository;
import com.dreamteam.corona.core.repository.RoleRepository;
import com.dreamteam.corona.core.repository.UserRepository;
import com.dreamteam.corona.quarantine.model.Quarantine;
import com.dreamteam.corona.quarantine.model.Ticket;
import com.dreamteam.corona.quarantine.repository.QuarantineRepository;
import com.dreamteam.corona.quarantine.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private QuarantineRepository quarantineRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);

        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        Role userRole = createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

//        User user = createUserIfNotFound("0048781611349",
//                "Dominik",
//                "Kuzaka",
//                "1234",
//                "dkuzaka@gmail.com",
//                Arrays.asList(userRole));
//
//        Date now = new Date();
//        LocalDateTime localNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//        Double latitude = 54.41552;
//        Double longitude = 18.555545;
//        Quarantine quarantine = createQuarantineIfNotFound(user, localNow, localNow.plusDays(14), latitude, longitude);
//
//        Ticket ticket = createTicket(quarantine, UUID.fromString("b2a9a1f5-2d91-48cc-b6dc-cce79a0ad1f7"));


        alreadySetup = true;
    }

    @Transactional
    Ticket createTicket(Quarantine quarantine, UUID token) {
        Ticket ticket = new Ticket();
        ticket.setQuarantine(quarantine);
        ticket.setToken(token);
        return ticketRepository.save(ticket);
    }

    @Transactional
    User createUserIfNotFound(String username, String firstName, String lastName, String pass, String email, List<Role> roles) {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(passwordEncoder.encode("testowe"));
            user.setEmail(email);
            user.setRoles(roles);
            user = userRepository.save(user);
        }
        return user;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            role = roleRepository.save(role);
        }
        return role;
    }

    @Transactional
    Quarantine createQuarantineIfNotFound(User user, LocalDateTime startDate, LocalDateTime endDate,
                                          Double latitude, Double longitude) {
        Quarantine quarantine = null; //quarantineRepository.getOne()
        if (quarantine == null) {
            quarantine = new Quarantine();
            quarantine.setUser(user);
            quarantine.setStartDate(startDate);
            quarantine.setEndDate(endDate);
            quarantine.setLatitude(latitude);
            quarantine.setLongitude(longitude);
            quarantine = quarantineRepository.save(quarantine);
        }
        return quarantine;
    }
}
