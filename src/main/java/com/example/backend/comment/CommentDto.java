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
public class CommentDto {

    private Long comment_id;
    private Long post_id;
    private Long user_id;
    private String comment_content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
