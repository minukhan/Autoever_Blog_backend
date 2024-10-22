package com.example.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userPw;
    private String userName;
    private String userEmail;
    private String userGender;
    private String userRefreshToken;
    private String userProfileImage;
    private String userNickname;
    private String userIntro;
    private String userVoiceId;
    private String userGithub;
    private String userInsta;
    private String userTwitter;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;
    private LocalDateTime deletedAt;


}
