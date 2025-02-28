package com.example.backend.playlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayListDto {
    private Long playlistId;
    private Long userId;
    private String userName; // 닉네임 필드 추가
    private Long postId;
    private String title; // 제목 필드 추가
    private String thumbnailUrl;
    private String audioUrl;
}
