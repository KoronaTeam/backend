package com.dreamteam.corona.quarantine.task;

import com.dreamteam.corona.quarantine.model.Quarantine;
import com.dreamteam.corona.quarantine.service.QuarantineService;
import com.dreamteam.corona.quarantine.service.TicketService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class ScheduledTasks {

    private final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private final TicketService ticketService;
    private final Environment env;

    @Transactional
    // @Scheduled(cron = "0 0/${task.frequency.minutes} ${task.start.hour}-${task.end.hour} * * *")
    @Scheduled(cron = "0/30 * * * * *")
    public void sendTicketOrders() {

        int ticketsDaily = Integer.parseInt(env.getProperty("task.tickets.daily"));
        int taskFrequencyInMinutes = Integer.parseInt(env.getProperty("task.frequency.minutes"));
        int taskStartHour = Integer.parseInt(env.getProperty("task.start.hour"));
        int taskEndHour = Integer.parseInt(env.getProperty("task.end.hour"));

        ticketService.prepareTicketForAllSendNotification(ticketsDaily, taskFrequencyInMinutes, taskEndHour);

    }
}
