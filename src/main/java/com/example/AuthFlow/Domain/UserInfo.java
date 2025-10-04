package com.example.AuthFlow.Domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name= "user_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String bio;
    private String location;

    private boolean sex;
    private LocalDateTime birth;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    protected UserInfo(String nickname, String bio, String location, Long id, boolean sex, LocalDateTime birth) {
        this.id = id;
        this.nickname = nickname;
        this.location = location;
        this.bio = bio;
        this.sex = sex;
        this.birth = birth;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
