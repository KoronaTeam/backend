package com.dreamteam.corona.quarantine.service;

import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.quarantine.model.Quarantine;
import com.dreamteam.corona.quarantine.model.Ticket;

import java.util.UUID;

public interface TicketService {

    Ticket findById(Long id);
    Ticket save(Ticket ticket);
    Ticket findByToken(UUID token);

    Double calculateDistance(Ticket ticket);

    void prepareTicketSendNotification(Quarantine quarantine);
    void prepareTicketForAllSendNotification(int ticketsDaily, int taskFrequencyInMinutes, int taskEndHour);
}
