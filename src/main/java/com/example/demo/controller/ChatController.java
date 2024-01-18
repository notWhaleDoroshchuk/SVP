package com.example.demo.controller;

import com.example.demo.dto.Message;
import com.example.demo.dto.User;
import com.example.demo.listener.WebSocketEventListener;
import com.example.demo.models.AuthUserRequest;
import com.example.demo.models.RegisterUserRequest;
import com.example.demo.services.UserService;
import com.example.demo.services.WebSocketTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @PostMapping("/authUser")
    public ResponseEntity<String> authUser(@RequestBody AuthUserRequest request) {
        if(userService.getUser(request).isPresent())
        return new ResponseEntity<>("Данные успешно обработаны", HttpStatus.OK);
        else return new ResponseEntity<>("Пользователь не найден", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/registerUser")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequest request){
        return userService.registerUser(request);
    }

}
