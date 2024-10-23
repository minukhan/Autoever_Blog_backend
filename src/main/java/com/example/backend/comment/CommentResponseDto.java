package com.example.backend.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Long commentId;
    private Long postId;
    private Long userId;
    private String commentContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
