package com.dreamteam.corona.core.service;

import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.quarantine.model.Quarantine;
import com.dreamteam.corona.quarantine.model.Ticket;

public interface EmailService {
    void sendRegistrationConfirmation(User user, String password);
    void sendQuarantineStartConfirmation(Quarantine quarantine);
    void sendTicketOrder(Ticket ticket);
}
