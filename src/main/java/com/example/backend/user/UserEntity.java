package com.example.backend.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserEntity {

    @Id
    private Long userId;

    private String userName;
    private String userRefreshToken;
    private String userProfileImage;
    private String userIntro;
    private String userVoiceId;
    private String userVoiceSelect;
    private String userGithub;
    private String userInsta;
    private String userTwitter;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;
    private LocalDateTime deletedAt;

    public void changeUserName(String userName) {
        this.userName = userName;
    }

    public void changeUserRefreshToken(String userRefreshToken) {
        this.userRefreshToken = userRefreshToken;
    }

    public void changeUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public void changeUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    public void changeUserVoiceId(String userVoiceId) {
        this.userVoiceId = userVoiceId;
    }

    public void changeUserVoiceSelect(String userVoiceSelect) {
        this.userVoiceSelect = userVoiceSelect;
    }

    public void changeUserGithub(String userGithub) {
        this.userGithub = userGithub;
    }

    public void changeUserInsta(String userInsta) {
        this.userInsta = userInsta;
    }

    public void changeUserTwitter(String userTwitter) {
        this.userTwitter = userTwitter;
    }

    public void changeCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void changeUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void changeDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void changeDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
