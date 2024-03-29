package com.example.demo.controller;

import com.example.demo.dto.Message;
import com.example.demo.models.*;
import com.example.demo.services.ChatsService;
import com.example.demo.services.UserService;
import com.example.demo.services.WebSocketTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatsService chatService;

    private final UserService userService;

    private final WebSocketTopicService webSocketTopicService;

//    @MessageMapping("/chat.sendMessage")
//    public Message sendMessage(@Payload Message chatMessage) {
//        webSocketTopicService.sendToTopic(chatMessage.getChat().getId().toString(), chatMessage);
//        return chatMessage;
//    }

    @PostMapping("/authUser")
    public ResponseEntity<String> authUser(@RequestBody AuthUserRequest request) {

        return userService.getUser(request)
                .map(user -> new ResponseEntity<>(user.getId().toString(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>("Пользователь не найден", HttpStatus.NOT_FOUND));
    }

    @PostMapping("/registerUser")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/createChat")
    public ResponseEntity<String> crateChat(@RequestBody CreateChatRequest request) {
        return chatService.createChat(request);
    }

    @GetMapping("/getAllChats/{userId}")
    public ResponseEntity<GetAllChatsResponse> getAllChats(@PathVariable String userId) {
        return new ResponseEntity<>(chatService.getAllUserChats(userId),  HttpStatus.OK);
    }

    @GetMapping("/getAllChatMessages/{chat_id}")
    public ResponseEntity getAllChatMessages(@PathVariable String chatIdStr) {
        return chatService.getAllChatMessages(chatIdStr);
    }

    @MessageMapping("/addMessage")
    public ResponseEntity addMessage(@RequestBody AddNewMessageRequest request){
        return chatService.addMessage(request);
    }
}
