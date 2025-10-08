package com.example.AuthFlow.Handler;

import com.example.AuthFlow.DTO.Chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class GameWebSocketHandler implements WebSocketHandler {

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
        var sessionId = session.getId();

        ChatMessage msg = ChatMessage.builder()
                .sender(sessionId)
                .receiver("all")
                .content(message.getPayload().toString())
                .build();
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
    }
}
