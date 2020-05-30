package com.dreamteam.corona.core.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Table(name = "privileges")
public class Privilege {

    public static final int PRIVILEGE_MAX_LENGTH = 30;
    public static final int PRIVILEGE_MIN_LENGTH = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty
    @Column(name = "name")
    @Length(min = PRIVILEGE_MIN_LENGTH, max = PRIVILEGE_MAX_LENGTH)
    private String name;

    public Privilege() {}

    public Privilege(String name) {
        this.name = name;
    }
}
