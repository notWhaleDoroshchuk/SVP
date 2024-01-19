package com.example.demo.services;

import com.example.demo.dao.ChatsRepository;
import com.example.demo.dao.MessagesRepository;
import com.example.demo.dto.Chat;
import com.example.demo.dto.Message;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ChatsService {

    private final ChatsRepository chatsRepository;

    private final MessagesRepository messagesRepository;

    public List<Chat> getAllUserChats(String userIdStr) {
        return chatsRepository.findAllByUserId(UUID.fromString(userIdStr));
    }

    public List<Message> getChatMessages(String id) {
        return chatsRepository
                .findById(UUID.fromString(id))
                .map(chat -> messagesRepository
                        .findAllByChat(chat))
                .orElseGet(ArrayList::new);
    }

    public void deleteChat(String userIdStr, String chatIdStr) {
        UUID userId = UUID.fromString(userIdStr);
        UUID chatId = UUID.fromString(chatIdStr);
        chatsRepository.deleteByIdAndUserId(chatId, userId);
    }

    public List<Chat> findChatsByName(String name, String userIdStr) {
        return chatsRepository.findChatsByName(name, UUID.fromString(userIdStr));
    }

}
