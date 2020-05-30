package com.dreamteam.corona.quarantine.model;

import com.dreamteam.corona.core.model.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "quarantines")
public class Quarantine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user")
    private User user;

    @OneToMany(mappedBy = "quarantine")
    private List<Ticket> tickets = new ArrayList<>();
    
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "push_token")
    private String pushToken;

    public long ticketsToday() {
        LocalDate today = LocalDate.now();
        long sum = tickets.stream().filter(t -> t.getCreated().toLocalDate().isEqual(today) ).count();
        return sum;
    }
}
