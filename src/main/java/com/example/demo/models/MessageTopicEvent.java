package com.example.demo.models;

import com.example.demo.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageTopicEvent {
    private String chatId;
    private String text;
    private MessageType type;
    private String login;
}
