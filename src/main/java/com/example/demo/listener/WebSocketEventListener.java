package com.example.demo.listener;

import com.example.demo.dto.Message;
import com.example.demo.enums.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.UUID;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userid = (String) headerAccessor.getSessionAttributes().get("userid");
        if(userid != null) {
            logger.info("User disconnected : " + username);
            Message chatMessage = new Message();
            chatMessage.setType(MessageType.LEAVE);
            chatMessage.setUserId(UUID.fromString(userid));
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
    public static void setLog (String name)
    {
        logger.info("User joined : " + name);
    }

}
