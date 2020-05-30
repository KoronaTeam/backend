package com.dreamteam.corona.core.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.LinkedHashSet;

@Entity
@Data
@Table(name = "roles")
public class Role {

    public static final int ROLE_MAX_LENGTH = 30;
    public static final int ROLE_MIN_LENGTH = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column
    @NotEmpty
    @Length(min = ROLE_MIN_LENGTH, max = ROLE_MAX_LENGTH)
    private String name;

    @Column(name = "is_locked")
    private boolean isLocked = false;

    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(
            name = "role_privileges",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges = new LinkedHashSet<>();

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }
}
