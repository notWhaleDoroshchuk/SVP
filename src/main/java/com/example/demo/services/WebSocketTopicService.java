package com.example.demo.services;

import com.example.demo.dto.Message;
import com.example.demo.models.MessageTopicEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketTopicService {

    private final  SimpMessagingTemplate messagingTemplate;

    public void sendToMessagesTopic(MessageTopicEvent payload) {
        messagingTemplate.convertAndSend("/topic/messages", payload);
    }

    public void sendToChatsTopic(){
        messagingTemplate.convertAndSend("/topic/chats", true);
    }
}
