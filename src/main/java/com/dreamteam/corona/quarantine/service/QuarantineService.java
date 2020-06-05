package com.dreamteam.corona.quarantine.service;

import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.quarantine.model.Quarantine;

import java.util.List;

public interface QuarantineService {

    Quarantine findById(Long id);
    Quarantine save(Quarantine quarantine);
    Quarantine findActiveQuarantineForUser(User user);


    Quarantine startQuarantineSendConfirmation(Quarantine quarantine);

    List<Quarantine> prepareListOfNeededTickets(Integer ticketsDaily);
}
