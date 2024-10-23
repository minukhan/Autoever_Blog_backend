package com.example.backend.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDto {
    private boolean isSuccess;
    private String message;

    private Data data;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data{
        private Long userId;
        private String name;
        private UserVoice voice;
        private String profileImg;
        private Social social;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Social{
        private String intro;
        private String github;
        private String instagram;
        private String twitter;
    }
}
