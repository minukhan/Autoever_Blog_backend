package com.example.backend.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostsDto {

    private Long postId;
    private Long userId;
    private String postTitle;
    private String postCategory;
    private String thumbnailUrl;
    private String postSummary;
    private String postContent;
    private String audioUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

    private String userName;

}