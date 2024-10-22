package com.example.backend.user;

import com.example.backend.domain.UserVoice;

import java.time.LocalDate;

public class UserDto {
    private Long user_id;
    private String user_pw;
    private String user_name;
    private String user_email;
    private String user_gender;
    private String user_refresh_token;

    private String user_profile_url;
    private String user_nickname;
    private String user_intro;
    private String user_voice_id;
    private UserVoice user_voice_select;
    private String user_github;
    private String user_insta;
    private String user_twitter;

    private LocalDate createdAt;
    private LocalDate updatedAt;
    private boolean isDeleted;
    private LocalDate deletedAt;
}
