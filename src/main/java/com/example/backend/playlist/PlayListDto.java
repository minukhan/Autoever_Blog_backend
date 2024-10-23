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
    private Long userId;
    private Long postId;
    private Long playlistId;
    private String thumbnailUrl;
    private String audioUrl;
}
