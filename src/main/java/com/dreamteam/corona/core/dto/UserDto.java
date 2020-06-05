package com.dreamteam.corona.core.dto;

import com.dreamteam.corona.core.model.Role;
import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.core.validator.EmailValidator;
import com.dreamteam.corona.quarantine.dto.QuarantineDto;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.LinkedHashSet;

@Data
public class UserDto {

    private Long id;

    @Length(min = User.USER_MIN_LENGTH, max = User.USER_MAX_LENGTH)
    @NotEmpty
    @NotNull
    private String username;

    @Length(min = User.USER_MIN_LENGTH, max = User.USER_MAX_LENGTH)
    @NotEmpty
    @NotNull
    private String password;

    @Length(min = User.USER_MIN_LENGTH, max = User.USER_MAX_LENGTH)
    @EmailValidator
    @NotEmpty
    private String email;

    @Length(min = User.USER_MIN_LENGTH, max = User.USER_MAX_LENGTH)
    @NotEmpty
    @NotNull
    private String firstName;

    @NotEmpty
    @NotNull
    @Length(min = User.USER_MIN_LENGTH, max = User.USER_MAX_LENGTH)
    private String lastName;

    private QuarantineDto quarantine;

    private boolean enabled;

    private boolean accountExpired;

    private boolean accountLocked;

    private boolean credentialExpired;

    public Collection<Role> roles = new LinkedHashSet<>();

    public Collection<? extends GrantedAuthority> authorities = new LinkedHashSet<>();

    public UserDto() {}

    public UserDto(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
