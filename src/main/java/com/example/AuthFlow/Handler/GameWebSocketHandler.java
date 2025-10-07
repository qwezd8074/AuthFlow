package com.example.AuthFlow.Handler;

import com.example.AuthFlow.DTO.Chat.ChatMessage;
import org.springframework.web.socket.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameWebSocketHandler implements WebSocketHandler {
    // key값: session ID - UUID 형식
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        var sessionId = session.getId();
        sessions.put(sessionId, session);

        ChatMessage message = ChatMessage.builder().sender(sessionId).receiver("all").build();
        message.newConnect();

        sessions.values().forEach(s -> {
            try {
                if(!s.getId().equals(sessionId)) {
                    s.sendMessage(new TextMessage(message.getContent()));
                }
            } catch (Exception e) {
                // TODO: throw;
            }
        });
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        var sessionId = session.getId();

        ChatMessage msg = ChatMessage.builder()
                .sender(sessionId)
                .receiver("all")
                .content(message.getPayload().toString())
                .build();

        sessions.values().forEach(s -> {
            try {
                if(!s.getId().equals(sessionId)) {
                    s.sendMessage(new TextMessage(msg.getContent()));
                }
            } catch (Exception e) {
                // TODO: throw;
            }
        });
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
