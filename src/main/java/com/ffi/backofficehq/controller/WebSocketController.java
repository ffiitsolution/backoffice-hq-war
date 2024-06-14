package com.ffi.backofficehq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/**")
    public void handleAnyMessage(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("Received message: " + message);

        // Inspect all headers to find the destination
        MessageHeaders headers = headerAccessor.getMessageHeaders();
        Object value = headers.get("simpDestination");
        String destination = "";
        if (value != null) {
            destination = (String) value;
            System.out.println("Destination: " + destination);
        }
        
        messagingTemplate.convertAndSend(destination.replace("/app", "/topic"), "Message received: " + message);
    }
}