package com.dreamteam.corona.quarantine.controller;

import com.dreamteam.corona.quarantine.service.PushNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController

public class TestController {

    private final String TOPIC = "topic123";
    private final PushNotificationService pushNotificationService;

    public TestController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> send() {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode body = mapper.createObjectNode();
        // body.put("registration_ids", ["qrgqry34562456"])
        // body.put("to", "/topics/" + TOPIC);
        body.put("to", "qrgqry34562456");
        body.put("priority", "high");

        ObjectNode notification = mapper.createObjectNode();
        notification.put("title", "COVID Notification");
        notification.put("body", "Important message");

        ObjectNode data = mapper.createObjectNode();
        data.put("Key-1", "Data 1");
        data.put("Key-2", "Data 2");

        body.set("notification", notification);
        body.set("data", data);

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = pushNotificationService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();
            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Push Notification Error!", HttpStatus.BAD_REQUEST);
    }
}
