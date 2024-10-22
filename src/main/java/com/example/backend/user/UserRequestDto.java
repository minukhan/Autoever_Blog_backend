package com.example.backend.user;

import com.example.backend.domain.UserVoice;
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
    private String profileImg;
    private String email;
    private String nickname;
    private UserVoice voiceselect;

    private String intro;
    private String github;
    private String insta;
    private String twitter;

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
