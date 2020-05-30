package com.dreamteam.corona.core.service;

import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.quarantine.model.Quarantine;
import com.dreamteam.corona.quarantine.model.Ticket;

public interface MailContentBuilder {
    String build(User user, String token);

    String buildRegistrationConfirmation(User user, String password);

    String buildQuarantineConfirmation(Quarantine quarantine);

    String buildTicketOrder(Ticket ticket);
}
