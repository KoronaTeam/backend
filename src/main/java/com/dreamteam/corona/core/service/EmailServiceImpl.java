package com.dreamteam.corona.core.service;

import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.core.security.CurrentUserDetailsService;
import com.dreamteam.corona.quarantine.model.Quarantine;
import com.dreamteam.corona.quarantine.model.Ticket;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service("emailService")
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private static final String FROM = "S.A.M. Covid19 Team<quarantine.team.bot@gmail.com>";

    private JavaMailSender javaMailSender;
    private MailContentBuilder mailContentBuilder;

    @Override
    @Async
    public void sendTicketOrder(Ticket ticket) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(FROM);
            messageHelper.setTo(ticket.getQuarantine().getUser().getEmail());
            messageHelper.setSubject("Żądanie potwierdzenia kwarantanny");
            String content = mailContentBuilder.buildTicketOrder(ticket);
            messageHelper.setText(content, true);
        };
        try {
            javaMailSender.send(messagePreparator);
            logger.info("Sent email: {}", "Żądanie potwierdzenia kwarantanny");
        } catch (MailException e) {
            System.out.println("MailException: " + e.getMessage());
        }
    }

    @Override
    @Async
    public void sendRegistrationConfirmation(User user, String password) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(FROM);
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("Potwierdzenie założenia konta w aplikacji S.A.M.");
            String content = mailContentBuilder.buildRegistrationConfirmation(user, password);
            messageHelper.setText(content, true);
        };
        try {
            javaMailSender.send(messagePreparator);
            logger.info("Sent email: {}", "Potwirdzenie założenia konta.");
        } catch (MailException e) {
            System.out.println("MailException: " + e.getMessage());
        }
    }

    @Override
    @Async
    public void sendQuarantineStartConfirmation(Quarantine quarantine) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(FROM);
            messageHelper.setTo(quarantine.getUser().getEmail());
            messageHelper.setSubject("Potwierdzenie rozpoczęcia kwarantanny S.A.M.");
            String content = mailContentBuilder.buildQuarantineConfirmation(quarantine);
            messageHelper.setText(content, true);
        };
        try {
            javaMailSender.send(messagePreparator);
            logger.info("Sent email: {}", "Potwirdzenie rozpoczęcia kwarantanny.");
        } catch (MailException e) {
            System.out.println("MailException: " + e.getMessage());
        }
    }
}
