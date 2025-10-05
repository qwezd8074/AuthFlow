package com.example.AuthFlow.DTO.Chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String type;
    private String sender;
    private String receiver;
    private String content;

    public void setSender(String sender) {this.sender = sender;}

    public void newConnect() {
        this.type = "new";
    }

    public void closeConnect() {
        this.type = "close";
    }

    @Override
    public String toString() {
        return this.content;
    }
}
