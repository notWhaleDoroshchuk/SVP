package com.example.demo.controller;

import com.example.demo.dto.Message;
import com.example.demo.dto.User;
import com.example.demo.listener.WebSocketEventListener;
import com.example.demo.service.UserService;
import com.example.demo.services.WebSocketTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    UserService userService;

    WebSocketTopicService webSocketTopicService;

    @MessageMapping("/chat.sendMessage")
    public Message sendMessage(@Payload Message chatMessage) {
        webSocketTopicService.sendToTopic(chatMessage.getChat().getId().toString(), chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.authUser")
    public User addUser(@Payload User user,
                               SimpMessageHeaderAccessor headerAccessor) {
        userService.save(user);
        headerAccessor.getSessionAttributes().put("userid", user.getId());
//        userService.getUser(chatMessage.getUserId()).ifPresent(user -> WebSocketEventListener.setLog(user.getLogin()));
//        webSocketTopicService.sendToTopic(chatMessage.getChat().getId().toString(), chatMessage);
        return user;
    }

}
