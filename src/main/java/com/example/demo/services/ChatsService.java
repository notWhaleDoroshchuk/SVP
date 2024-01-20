package com.example.demo.services;

import com.example.demo.dao.ChatsRepository;
import com.example.demo.dao.MessagesRepository;
import com.example.demo.dao.UsersRepository;
import com.example.demo.dto.Chat;
import com.example.demo.dto.Message;
import com.example.demo.dto.User;
import com.example.demo.enums.MessageType;
import com.example.demo.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatsService {
    private final ChatsRepository chatsRepository;

    private final MessagesRepository messagesRepository;
    private final UsersRepository userRepository;
    private final WebSocketTopicService webSocketTopicService;

    public ResponseEntity<String> createChat(CreateChatRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        var chat = new Chat();
        chat.setChatName(request.getName());
        chat.setUserId(userId);
        chat.setCreated(Instant.now());
        chat.setLastUpdateDate(Instant.now());
        var chatOpt = chatsRepository.findByChatName(request.getName());
        if (chatOpt.isPresent())
            return new ResponseEntity<>("Чат с таким названием уже существует", HttpStatus.BAD_REQUEST);
        else {
            chatsRepository.save(chat);
            return new ResponseEntity<>(chat.getId().toString(), HttpStatus.OK);
        }
    }

    public GetAllChatsResponse getAllUserChats(String userIdStr) {
        var chatDataList = chatsRepository
                .findAllByUserId(UUID.fromString(userIdStr))
                .stream()
                .map(chat -> new ChatData(chat.getId().toString(),
                        chat.getChatName(),
                        chat.getMessages().size() > 0 ?
                                chat.getMessages()
                                        .stream()
                                        .sorted(Comparator.comparing(Message::getCreated))
                                        .toList()
                                        .get(chat.getMessages().size() - 1)
                                        .getText() : ""))
                .toList();

        return new GetAllChatsResponse(chatDataList);
    }

    public List<Message> getChatMessages(String id) {
        return chatsRepository
                .findById(UUID.fromString(id))
                .map(messagesRepository::findAllByChat)
                .orElseGet(ArrayList::new);
    }

    public void deleteChat(String userIdStr, String chatIdStr) {
        UUID userId = UUID.fromString(userIdStr);
        UUID chatId = UUID.fromString(chatIdStr);
        chatsRepository.deleteByIdAndUserId(chatId, userId);
    }

    public List<Chat> findChatsByName(String name, String userIdStr) {
        return chatsRepository.findChatsByNameAndUserId(name, UUID.fromString(userIdStr));
    }

    public ResponseEntity getAllChatMessages(String chatIdStr) {
        var chatId = UUID.fromString(chatIdStr);
        var chat = chatsRepository.findById(chatId);
        if (chat.isPresent())
            return new ResponseEntity<>(chat.get().getMessages(), HttpStatus.OK);
        else
            return new ResponseEntity<>("Чат не найден", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity addMessage(AddNewMessageRequest request) {
        var chatId = UUID.fromString(request.getChatId());
        var userId = UUID.fromString(request.getUserId());
        var chat = chatsRepository.findById(chatId);
        var message = new Message();
        MessageTopicEvent event;
        message.setChat(chat.get());
        message.setUserId(userId);
        message.setText(request.getText());
        message.setType(request.getType());
        messagesRepository.save(message);
        event = buildMessageTopicEvent(userId, request.getType(), "", chatId);
        webSocketTopicService.sendToMessagesTopic(event);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    private MessageTopicEvent buildMessageTopicEvent(UUID userId, MessageType type, String text, UUID chatId) {
        var user = userRepository.findById(userId);
        var event = new MessageTopicEvent();
        var login = user.get().getLogin();
        if(type == MessageType.JOIN) text = "Пользователь " + login + "присоединился к чату";
        event.setLogin(login);
        event.setText(text);
        event.setType(type);
        event.setChatId(chatId.toString());
        return event;
    }

}
