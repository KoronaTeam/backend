package com.dreamteam.corona.quarantine.exception;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(Long id) {
        super("Could not found Ticket:" + id);
    }
    public TicketNotFoundException(String message) { super(message); }
}
