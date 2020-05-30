package com.dreamteam.corona.quarantine.service;

import com.dreamteam.corona.core.service.EmailServiceImpl;
import com.dreamteam.corona.quarantine.model.HeaderRequestInterceptor;
import com.dreamteam.corona.quarantine.model.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service("pushNotificationService")
public class PushNotificationServiceImpl implements PushNotificationService {
    private static final Logger logger = LoggerFactory.getLogger(PushNotificationServiceImpl.class);
    private static final String FIREBASE_SERVER_KEY = "AAAAudaI4x0:APA91bGrbce8lPMT_LV32bwwOmwSXR8itcjUZ-pbkZqbe-51wgNBZf2ZBdmoGbm8olzKAkNu4zQdjNPwdLmzO59vo1-N8RDyOZTkbyioIFzc9RdBEF6UuBKMbULD9_ssah6H6qhbufZf";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";


    @Override
    @Async
    public CompletableFuture<String> send(HttpEntity<String> entity) {
        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }

    @Override
    public void sendTicketOrder(Ticket ticket) {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode body = mapper.createObjectNode();
        // body.put("registration_ids", ["qrgqry34562456"])
        // body.put("to", "/topics/" + TOPIC);
        body.put("to", ticket.getQuarantine().getPushToken());
        body.put("priority", "high");

        ObjectNode notification = mapper.createObjectNode();
        notification.put("title", "COVID Notification");
        notification.put("body", "Ticket request.");

        ObjectNode data = mapper.createObjectNode();
        data.put("TicketToken", ticket.getToken().toString());

        body.set("notification", notification);
        body.set("data", data);

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();
            logger.info("Wysłano komunikat push");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return;
    }

}
