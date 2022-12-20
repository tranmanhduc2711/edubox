package com.example.edubox.controller;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.module.Configuration;

@RestController
@RequestMapping(value = "/health")
public class HealthController {
    @GetMapping
    public String healthCheck() {
//        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
//        config.setHostname("localhost");
//        config.setPort(1433);
//
//        SocketIOServer server = new SocketIOServer(config);
//
//        server.addConnectListener(
//                (client) -> {
//                    System.out.println("Client has Connected!");
//                });
//
//        server.addEventListener("MESSAGE", String.class,
//                (client, message, ackRequest) -> {
//                    System.out.println("Client said: " + message);
//                });
//
//        server.start();
        return "OK";
    }
}
