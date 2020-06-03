package com.dreamteam.corona.core.service;

import com.dreamteam.corona.quarantine.model.Ticket;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

public interface PushNotificationService {

    public CompletableFuture<String> send(HttpEntity<String> entity);
    public void sendTicketOrder(Ticket ticket);
}
