package com.ffi.backofficehq.controller;

import com.ffi.paging.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

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

    @Scheduled(cron = "*/5 * * * * *")
    public void wsTimePer5Second() {
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fTime = currentTime.format(formatter);

            String response = "{ \"serverTime\": \"" + fTime + "\" }";
            messagingTemplate.convertAndSend("/topic/outlets", response);
        } catch (NumberFormatException | MessagingException e) {
            System.err.println("boffihq || wsTimePer5Second error: " + e.getMessage());
        }
    }

    @PostMapping(path = "/api/outletMessage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Send Message", description = "Send Message")
    public @ResponseBody
    ResponseMessage sendMessage(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        System.out.println("param: " + param);

        String outletCode = balance.get("outletCode");
        String message = balance.get("message");
        simpMessagingTemplate.convertAndSend("/topic/outlets/" + outletCode, message);
        ResponseMessage rm = new ResponseMessage();
        rm.setSuccess(true);
        return rm;
    }
}