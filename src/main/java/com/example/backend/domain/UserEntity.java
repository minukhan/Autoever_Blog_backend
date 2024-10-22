package com.example.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "user_pw")
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
