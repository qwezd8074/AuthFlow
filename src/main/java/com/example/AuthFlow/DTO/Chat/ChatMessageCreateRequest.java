package com.example.AuthFlow.DTO.Chat;

import lombok.Getter;

@Getter
public class ChatMessageCreateRequest {
    private Long from;
    private Long to;
    private String content;
}
