package com.example.AuthFlow.Websocket;

import com.example.AuthFlow.DTO.Chat.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class GameWebSocketHandler implements WebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    // session ID가 이미 할당되어 있는지 확인하기 위한 값
    private final Map<String, String> sessionRoomId = new ConcurrentHashMap<>();

    // key값: RoomID - 정수(ex. 1, 2, 3) -> key값: sessionID - UUID 형식, val 값: RoomID
    private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        var str = String.format("session 연결: %s", session.getId());
        log.info(str);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Map<String, String> payload = objectMapper.readValue(message.getPayload().toString(), Map.class);

        log.info(payload.get("type").toString());

        var type =  payload.get("type");

        switch (type) {
            case "join" -> {
                var groupId = payload.get("groupId").toString();
                this.join(groupId, session);
            }
            case "exit" -> {
                this.exit(session);
            }
        }
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

    private void join(String groupId, WebSocketSession session) {
        roomSessions
                .computeIfAbsent(groupId, key -> ConcurrentHashMap.newKeySet())
                .add(session);

        sessionRoomId.put(session.getId(), groupId);

        displayCurrentSessionStatus();
    }

    private void exit(WebSocketSession session) {
        var groupId = sessionRoomId.get(session.getId());

        if (groupId == null) {
            throw new NoSuchElementException();
        }

        roomSessions.computeIfPresent(
                groupId,
                (key, innerSession) -> {
                    innerSession.remove(session);
                    return innerSession;
                }
        );
        sessionRoomId.remove(session.getId());

        displayCurrentSessionStatus();
    }

    private void displayCurrentSessionStatus(){
        log.info(sessionRoomId.values().toString());
        log.info(roomSessions.values().toString());
    }
}
