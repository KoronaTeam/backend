package com.dreamteam.corona.quarantine.service;

import com.dreamteam.corona.core.service.EmailService;
import com.dreamteam.corona.core.service.PushNotificationService;
import com.dreamteam.corona.quarantine.exception.TicketNotFoundException;
import com.dreamteam.corona.quarantine.model.Quarantine;
import com.dreamteam.corona.quarantine.model.Ticket;
import com.dreamteam.corona.quarantine.repository.QuarantineRepository;
import com.dreamteam.corona.quarantine.repository.TicketRepository;
import com.dreamteam.corona.quarantine.task.ScheduledTasks;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("ticketService")
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private final TicketRepository ticketRepository;
    private final QuarantineRepository quarantineRepository;
    private final EmailService emailService;
    private final PushNotificationService pushNotificationService;

    @Override
    public Ticket findById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket findByToken(UUID token) {
        return ticketRepository.findByToken(token)
                .orElseThrow(() -> new TicketNotFoundException("Could not found Ticket with token: " + token));
    }

    @Override
    public Double calculateDistance(Ticket ticket) {

        Double quarantineLatitude = ticket.getQuarantine().getLatitude();
        Double quarantineLongitude = ticket.getQuarantine().getLongitude();
        Double recentLatitude = ticket.getLatitude();
        Double recentLongitude = ticket.getLongitude();

        if (quarantineLatitude == recentLatitude && quarantineLongitude == recentLongitude) {
            return 0D;
        } else {
            double theta = quarantineLongitude - recentLongitude;
            double distance = Math.sin(Math.toRadians(quarantineLatitude)) * Math.sin(Math.toRadians(recentLatitude))
                    + Math.cos(Math.toRadians(quarantineLatitude)) * Math.cos(Math.toRadians(recentLatitude))
                    * Math.cos(Math.toRadians(theta));
            distance = Math.acos(distance);
            distance = Math.toDegrees(distance);
            distance = distance * 60 * 1.1515;

            // distance in meters
            distance = distance * 1609.344;

            return (distance);
        }
    }


    public void prepareTicketForAllSendNotification(int ticketsDaily, int taskFrequencyInMinutes, int taskEndHour) {

        List<Quarantine> activeQuarantines = quarantineRepository.findAllByActiveTrue();
        List<Quarantine> notRealizedQuarantines = activeQuarantines.stream()
                .filter(q -> q.ticketsToday() < ticketsDaily && q.getPushToken() != null)
                .collect(Collectors.toList());

        if(notRealizedQuarantines.size() == 0) return;

        Long numOfRealized = notRealizedQuarantines.stream()
                .mapToLong(Quarantine::ticketsToday).sum();
        Long numOfLeftTickets = activeQuarantines.size() * ticketsDaily - numOfRealized;

        int hourNow = LocalDateTime.now().getHour();
        int tasksLeft = (taskEndHour - hourNow) * 60 / taskFrequencyInMinutes;
        long ticketsInBatch = (numOfLeftTickets + tasksLeft -1) / tasksLeft; //ceil

        Random rand = new Random();
        List<Quarantine> quarantinesToUse = new ArrayList<>();
        if (notRealizedQuarantines.size()>0) {
            while(quarantinesToUse.size() < ticketsInBatch) {
                int n;
                if (notRealizedQuarantines.size()  < 2 ) {
                    n = 0;
                } else {
                    n = rand.nextInt(notRealizedQuarantines.size());
                }

                Quarantine q = notRealizedQuarantines.get(n);
                boolean isInList = quarantinesToUse.contains(q);
                if (!isInList){
                    quarantinesToUse.add(q);
                }
            }
        }

        log.info("Pozostało: {}", numOfLeftTickets);
        for(Quarantine quarantine : quarantinesToUse) {
            prepareTicketSendNotification(quarantine);
            log.info("Wysłano email {}", quarantine.getUser().getLastName() );
        }
    }

    @Override
    public void prepareTicketSendNotification(Quarantine quarantine) {
        Ticket ticket =  new Ticket();
        ticket.setToken(UUID.randomUUID());
        ticket.setQuarantine(quarantine);
        quarantine.getTickets().add(ticket);
        ticket.setCreated(LocalDateTime.now());
        ticket = save(ticket);

        log.info("Send inforamtion to: {} {}", ticket.getQuarantine().getUser().getId(), ticket.getQuarantine().getUser().getEmail());
        emailService.sendTicketOrder(ticket);
        pushNotificationService.sendTicketOrder(ticket);
    }
}
