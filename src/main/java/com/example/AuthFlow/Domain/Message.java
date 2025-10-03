package com.example.AuthFlow.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.UUID;

@Entity
@Table(name="message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {
    @Id
    private String id;

    // 메시지 보내는 유저
    private Long from;

    // 메시지 받는 유저
    private Long to;

    // 컨텐츠
    private String content;

    private LocalDateTime createdAt;

    @Builder
    private Message(Long from, Long to, String content){
        this.id = this.generateId();
        this.from = from;
        this.to = to;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    private String generateId(){
        long currentDate = LocalDateTime
                .now()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        String random = UUID.randomUUID().toString().substring(0,8);

        // 밀리초 단위 UNIX 시간 + UUID 랜덤 문자열
        return String.format("%d-%s", currentDate, random);
    }
}
