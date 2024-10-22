package com.example.backend.domain;

import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private Long postId;
    private Long userId;
    private String commentContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
