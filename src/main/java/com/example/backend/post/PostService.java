package com.example.backend.post;


import com.example.backend.domain.PostEntity;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Builder
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long createPost(PostDto postDto){
        PostEntity postEntity = PostEntity.builder()
                .userId(postDto.getUserId())
                .postTitle(postDto.getPostTitle())
                .postCategory(postDto.getPostCategory())
                .thumbnailUrl(postDto.getThumbnailUrl())
                .postSummary(postDto.getPostSummary())
                .postContent(postDto.getPostContent())
                .audioUrl(postDto.getAudioUrl())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        PostEntity savedPost = postRepository.save(postEntity);
        return savedPost.getPostId();

    }

    //게시글 조회



}
