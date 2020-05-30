package com.dreamteam.corona.quarantine.repository;


import com.dreamteam.corona.quarantine.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByToken(UUID token);

}
