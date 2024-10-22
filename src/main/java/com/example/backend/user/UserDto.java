package com.example.backend.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long user_id;

    private String user_pw;
    private String user_name;
    private String user_email;
    private String user_gender;
    private String user_refresh_token;
    private String user_profile_image;
    private String user_nickname;
    private String user_intro;
    private String user_voice_id;
    private String user_github;
    private String user_insta;
    private String user_twitter;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;
    private LocalDateTime deletedAt;

}
