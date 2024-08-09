package com.ffi.backofficehq;

import com.ffi.backofficehq.services.ViewServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Autowired
    ViewServices viewServices;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private void printLogOut(String message) {
        System.out.println(LocalDateTime.now().format(dateTimeFormatter) + " || " + message);
    }

    @MessageMapping("/**")
    public void handleAnyMessage(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        if (!message.isBlank()) {
            printLogOut("handleAnyMessage: Received message: " + message);

            // Inspect all headers to find the destination
            MessageHeaders headers = headerAccessor.getMessageHeaders();
            Object value = headers.get("simpDestination");
            String destination = "";
            if (value != null) {
                destination = (String) value;
                printLogOut("handleAnyMessage: Destination: " + destination);
            }

            messagingTemplate.convertAndSend(destination.replace("/app", "/topic"), message);
        }
    }

    @Scheduled(cron = "*/1 * * * * *")
    public void wsTimePerSecond() {
        try {
            String beVersion = viewServices.versionBe;
            String feVersion = viewServices.versionFe;

            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fTime = currentTime.format(formatter);

            String response = "{ \"serverTime\": \"" + fTime + "\", \"beVersion\": \"" + beVersion + "\", \"feVersion\": \"" + feVersion + "\" }";
            messagingTemplate.convertAndSend("/topic/serverTime", response);
        } catch (NumberFormatException | MessagingException e) {
            printLogOut("wsTimePerSecond error: " + e.getMessage());
        }
    }

    // tiap 5 detik cek outlet yg aktif
    @Scheduled(cron = "*/5 * * * * *")
    public void wsTimePer5Second() {
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fTime = currentTime.format(formatter);

            String response = "{ \"action\": \"time\", \"serverTime\": \"" + fTime + "\" }";
            messagingTemplate.convertAndSend("/topic/outlets", response);
        } catch (NumberFormatException | MessagingException e) {
            printLogOut("wsTimePer5Second error: " + e.getMessage());
        }
    }

    @PostMapping(path = "/api/outletMessage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Send Message", description = "Send Message")
    public @ResponseBody
    Map<String, Object> sendMessage(@RequestBody String param) throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());
        printLogOut("sendMessage: " + param);

        String outletCode = balance.get("outletCode");
        String message = balance.get("message");
        simpMessagingTemplate.convertAndSend("/topic/outlets/" + outletCode, message);
        Map<String, Object> rm = new HashMap();
        rm.put("success", true);
        return rm;
    }
}
