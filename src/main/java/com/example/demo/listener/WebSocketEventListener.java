package com.example.demo.listener;

import com.example.demo.dao.UsersRepository;
import com.example.demo.dto.Message;
import com.example.demo.enums.MessageType;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    private SimpMessageSendingOperations messagingTemplate;

    private UserService userService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userid = (String) headerAccessor.getSessionAttributes().get("userid");
        if(userid != null) {
            userService.getUser(UUID.fromString(userid)).ifPresent(user -> {
                logger.info("User disconnected : " + user.getLogin());
                Message chatMessage = new Message();
                chatMessage.setType(MessageType.LEAVE);
                chatMessage.setUserId(user.getId());
                messagingTemplate.convertAndSend("/topic/public", chatMessage);
            });
        }
    }
    public static void setLog (String name)
    {
        logger.info("User joined : " + name);
    }

}
