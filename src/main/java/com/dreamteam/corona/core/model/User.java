package com.dreamteam.corona.core.model;

import com.dreamteam.corona.core.validator.EmailValidator;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.LinkedHashSet;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {

    public static final int USER_MAX_LENGTH = 50;
    public static final int USER_MIN_LENGTH = 5;

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    @Length(min = USER_MIN_LENGTH, max = USER_MAX_LENGTH)
    @NotBlank
    private String username;

    @Column(name = "password", nullable = false)
    //@Length(min = USER_MIN_LENGTH, max = USER_MAX_LENGTH)
    @NotBlank
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    @Length(min = USER_MIN_LENGTH, max = USER_MAX_LENGTH)
    @NotBlank
    @EmailValidator
    private String email;

    @Column(name = "first_name")
    @NotBlank
    @Length(min = USER_MIN_LENGTH, max = USER_MAX_LENGTH)
    private String firstName;

    @Column(name = "last_name")
    @NotBlank
    @Length(min = USER_MIN_LENGTH, max = USER_MAX_LENGTH)
    private String lastName;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "account_expired")
    private boolean accountExpired;

    @Column(name = "account_locked")
    private boolean accountLocked;

    @Column(name = "credential_expired")
    private boolean credentialExpired;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
        // ToDo: check what this class should extend
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialExpired;
    }

    public User() {}

    public User(String username, String password, String firstName, String lastName, String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

    }

    public boolean hasRole(String targetRole) {
        if (targetRole == null) {
            return false;
        }
        if (roles == null) {
            System.out.println("authorities is null for user " + this);
        }

        for (Role role : roles) {
            if (targetRole.equals(role.getName())) {
                return true;
            }
        }
        return false;
    }

}
