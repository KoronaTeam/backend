package com.dreamteam.corona.quarantine.service;

import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.core.service.EmailService;
import com.dreamteam.corona.quarantine.exception.QuarantineNotFoundException;
import com.dreamteam.corona.quarantine.model.Quarantine;
import com.dreamteam.corona.quarantine.model.Ticket;
import com.dreamteam.corona.quarantine.repository.QuarantineRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service("quarantineService")
@AllArgsConstructor
public class QuarantineServiceImpl implements QuarantineService {

    private final QuarantineRepository quarantineRepository;
    private EmailService emailService;

    @Override
    public Quarantine findById(Long id) {
        return quarantineRepository.findById(id)
                .orElseThrow(() -> new QuarantineNotFoundException(id));
    }

    @Override
    public Quarantine save(Quarantine quarantine) {
        return quarantineRepository.save(quarantine);
    }

    @Override
    public Quarantine findActiveQuarantineForUser(User user) {
        return quarantineRepository.findByUserAndActiveTrue(user)
        .orElseThrow(() -> new QuarantineNotFoundException("Nie odnaleziono aktywnej kwarantanny dla użytkownika " + user.getUsername()));
    }

    @Override
    public Quarantine startQuarantineSendConfirmation(Quarantine quarantine) {
        emailService.sendQuarantineStartConfirmation(quarantine);
        return save(quarantine);
    }

    @Override
    public List<Quarantine> prepareListOfNeededTickets(Integer ticketsDaily) {

        List<Quarantine> quarantines = quarantineRepository.findAllByActiveTrue();
        return quarantines;
    }

}
