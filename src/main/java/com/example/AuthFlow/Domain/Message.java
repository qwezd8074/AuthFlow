package com.example.AuthFlow.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.UUID;

@Entity
@Table(name="message")
public class Message {
    @Id
    final String id;

    // 메시지 보내는 유저
    @Getter()
    @Setter()
    private Long from;

    // 메시지 받는 유저
    @Getter()
    @Setter()
    private Long to;

    // 컨텐츠
    @Getter()
    @Setter()
    private String content;

    @Getter()
    final LocalDateTime createdAt;

    Message(Long from, Long to, String content){
        this.id = this.createId();
        this.from = from;
        this.to = to;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    private String createId(){
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
