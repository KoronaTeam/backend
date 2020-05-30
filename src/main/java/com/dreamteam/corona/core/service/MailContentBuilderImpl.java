package com.dreamteam.corona.core.service;

import com.dreamteam.corona.core.model.User;
import com.dreamteam.corona.quarantine.model.Quarantine;
import com.dreamteam.corona.quarantine.model.Ticket;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service("mailContenetBuilder")
public class MailContentBuilderImpl implements MailContentBuilder {

    private TemplateEngine templateEngine;

    public MailContentBuilderImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String build(User user, String token) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("token", token);
        return templateEngine.process("testMailTemplate", context);
    }

    @Override
    public String buildRegistrationConfirmation(User user, String password) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("password", password);
        return templateEngine.process("registrationConfirmation", context);
    }

    @Override
    public String buildQuarantineConfirmation(Quarantine quarantine) {
        Context context = new Context();
        context.setVariable("quarantine", quarantine);
        return templateEngine.process("quarantineConfirmation", context);
    }

    @Override
    public String buildTicketOrder(Ticket ticket) {
        Context context = new Context();
        context.setVariable("ticket", ticket);
        return templateEngine.process("ticketOrder", context);
    }
}
