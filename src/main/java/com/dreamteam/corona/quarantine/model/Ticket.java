package com.dreamteam.corona.quarantine.model;

import com.dreamteam.corona.core.model.Role;
import com.dreamteam.corona.core.model.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "tickets")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name="quarantine")
    private Quarantine quarantine;

    @Column(name = "token")
    @org.hibernate.annotations.Type(type="uuid-char")
    private UUID token;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}
