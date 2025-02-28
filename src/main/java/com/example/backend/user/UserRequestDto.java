package com.example.backend.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    private Long userId;
    private String name;
    private UserVoice voiceSelect;

    private String intro;
    private String github;
    private String insta;
    private String twitter;

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
