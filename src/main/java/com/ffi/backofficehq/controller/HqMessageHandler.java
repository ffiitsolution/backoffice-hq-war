/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.backofficehq.controller;

import java.util.HashSet;
import java.util.Set;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class HqMessageHandler extends TextWebSocketHandler {
    

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("payload: " + payload);
        super.handleTextMessage(session, message);
        
    }
        // Handle messages sent to "/ws" endpoint
//        if (session.get/HandshakeInfo().getRequestURI().toString().equals("/ws")) {
        // Handle messages specific to "/ws" endpoint
        // Example: Log the message
//            System.out.println(/"Message received from /ws endpoint: " + payload);
//        } else {
        // Handle messages from other endpoints
//        super.handleTextMessage(session, message);
//        }
//    }
    
}
