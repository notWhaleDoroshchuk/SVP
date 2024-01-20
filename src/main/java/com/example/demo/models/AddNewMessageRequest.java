package com.example.demo.models;

import com.example.demo.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddNewMessageRequest {
    private String userId;
    private String chatId;
    private String text;
    private MessageType type;
}
