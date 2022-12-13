package com.example.edubox.controller;

import com.example.edubox.constant.EMessageEvent;
import com.example.edubox.entity.User;
import com.example.edubox.model.res.SlideRes;
import com.example.edubox.service.SlideService;
import com.example.edubox.service.UserService;
import com.example.edubox.socket.Message;
import com.example.edubox.socket.req.NextEventPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {
    @Autowired
    private SlideService slideService;
    @Autowired
    private UserService userService;

    private  ObjectMapper objectMapper = new ObjectMapper();

    @MessageMapping("/start")
    @SendTo("/edubox-client")
    public Message sendMessage(@Payload Message msg) {
        if(EMessageEvent.NEXT.equals(msg.getEvent())) {
            Message<SlideRes> msgRes = new Message<>();
            NextEventPayload payload = (NextEventPayload) msg.getPayload();
            SlideRes slideRes = slideService.getSlide(payload.getPresentCode(),payload.getItemNo());
            msgRes.setPayload(slideRes);
            return msgRes;
        }
        return msg;
    }

    @MessageMapping("/join")
    @SendTo("/topic/public")
    public Message addUser(@Payload Message chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(principal);
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", user.getUsername());
        return chatMessage;
    }

}
