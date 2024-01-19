package com.example.demo.services;

import com.example.demo.dao.ChatsRepository;
import com.example.demo.dao.MessagesRepository;
import com.example.demo.dto.Chat;
import com.example.demo.dto.Message;
import com.example.demo.models.ChatData;
import com.example.demo.models.CreateChatRequest;
import com.example.demo.models.GetAllChatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ChatsService {
    private final ChatsRepository chatsRepository;

    private final MessagesRepository messagesRepository;
    public ResponseEntity<String> createChat(CreateChatRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        var chat = new Chat();
        chat.setChatName(request.getName());
        chat.setUserId(userId);
        chat.setCreated(Instant.now());
        chat.setLastUpdateDate(Instant.now());
        var chatOpt = chatsRepository.findByChatName(request.getName());
        if(chatOpt.isPresent())
            return  new ResponseEntity<>("Чат с таким названием уже существует", HttpStatus.BAD_REQUEST);
        else{
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
                        chat.getMessages()
                                .stream()
                                .sorted(Comparator.comparing(Message::getCreated))
                                .toList()
                                .get(chat.getMessages().size()-1)
                                .getText()))
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

}
