package com.ffi.backofficehq.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author USER
 */
@RestController
public class SchedulerController {
    public final HealthEndpoint healthEndpoint;

    @Autowired
    public SimpMessagingTemplate wsTemplate;

    public SchedulerController(org.springframework.boot.actuate.health.HealthEndpoint healthEndpoint) {
        this.healthEndpoint = healthEndpoint;
    }
    
    @Scheduled(cron = "*/1 * * * * *")
    public void wsTimePerSecond() {
            try {
                // Define output format
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String fTransDate = currentDate.format(outputFormatter);

                LocalTime currentTime = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String fTime = currentTime.format(formatter);

                Status status = healthEndpoint.health().getStatus();
                String healthStatus = status.getCode();

                String response = "{ \"date\": \"" + fTransDate + "\", \"time\": \"" + fTime + "\", \"health\": \"" + healthStatus + "\" }";
                wsTemplate.convertAndSend("/topic/serverTime", response);
            } catch (NumberFormatException | MessagingException e) {
                System.err.println("wsTimePerSecond: " + e.getMessage());
            }
    }
}
