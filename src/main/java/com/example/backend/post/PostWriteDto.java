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
public class PostWriteDto {

    private Long userId;
    private String postTitle;
    private String postCategory;
    private String postSummary;
    private String postContent;
//    private String thumbnailUrl;

}