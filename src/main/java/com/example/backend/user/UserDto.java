package com.example.backend.user;

import java.time.LocalDate;

public class UserDto {
    private Long user_id;
    private String user_name;
    private String user_refresh_token;

    private String user_profile_url;
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
