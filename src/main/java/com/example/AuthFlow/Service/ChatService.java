package com.example.AuthFlow.Service;

import com.example.AuthFlow.DTO.Chat.ChatMessageCreateRequest;
import com.example.AuthFlow.Domain.Message;
import com.example.AuthFlow.Repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatService {
    private final MessageRepository messageRepository;

    @Autowired
    public ChatService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public boolean saveMessage(ChatMessageCreateRequest chatMessageCreateRequest){
        Message msg = Message.builder()
                .sender(chatMessageCreateRequest.getTo())
                .receiver(chatMessageCreateRequest.getFrom())
                .content(chatMessageCreateRequest.getContent())
                .build();

        try {
            this.messageRepository.save(msg);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
