package com.example.backend.post;


import com.example.backend.domain.PostEntity;
import com.example.backend.user.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
public class PostService {

    private final PostRepository postRepository;

    // 게시글 작성
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
    @Transactional
    public List<PostDto> getAllPosts(){
        List<PostEntity> posts = postRepository.findAll();
        return posts.stream().map(post -> PostDto.builder()
                        .postId(post.getPostId())
                        .userId(post.getUserId())
                        .postTitle(post.getPostTitle())
                        .postCategory(post.getPostCategory())
                        .thumbnailUrl(post.getThumbnailUrl())
                        .postSummary(post.getPostSummary())
                        .postContent(post.getPostContent())
                        .audioUrl(post.getAudioUrl())
                        .isDeleted(post.getIsDeleted())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    //특정 게시글 조회
    public PostDto getPostById(@PathVariable Long postId){
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return PostDto.builder()
                .postId(post.getPostId())
                .userId(post.getUserId())
                .postTitle(post.getPostTitle())
                .postCategory(post.getPostCategory())
                .thumbnailUrl(post.getThumbnailUrl())
                .postSummary(post.getPostSummary())
                .isDeleted(post.getIsDeleted())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    //유저 아이디 별 게시글 조회
    @Transactional
    public List<PostDto> getUserPosts(Long userId){
        List<PostEntity> posts = postRepository.findByUserId(userId);
        return posts.stream().map(post -> PostDto.builder()
                        .postId(post.getPostId())
                        .userId(post.getUserId())
                        .postTitle(post.getPostTitle())
                        .postCategory(post.getPostCategory())
                        .thumbnailUrl(post.getThumbnailUrl())
                        .postSummary(post.getPostSummary())
                        .postContent(post.getPostContent())
                        .audioUrl(post.getAudioUrl())
                        .isDeleted(post.getIsDeleted())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    //게시글 수정
    @Transactional
    public PostDto updatePost(Long postId, PostDto postDto){

        Optional<PostEntity> optionalPost = postRepository.findById(postId);

        // 게시글이 존재하지 않으면 예외 발생
        if (optionalPost.isEmpty()) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다.");
        }

        PostEntity post = optionalPost.get();
        post.setPostTitle(postDto.getPostTitle());
        post.setPostCategory(postDto.getPostCategory());
        post.setThumbnailUrl(postDto.getThumbnailUrl());
        post.setPostContent(postDto.getPostContent());
        post.setAudioUrl(postDto.getAudioUrl());
        post.setUpdatedAt(LocalDateTime.now()); //수정된 시간 업데이트

        postRepository.save(post);

        return PostDto.builder()
                .postId(post.getPostId())
                .userId(post.getUserId())
                .postTitle(post.getPostTitle())
                .postCategory(post.getPostCategory())
                .thumbnailUrl(post.getThumbnailUrl())
                .postSummary(post.getPostSummary())
                .postContent(post.getPostContent())
                .audioUrl(post.getAudioUrl())
                .isDeleted(post.getIsDeleted())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
  }

    //게시글 삭제
    @Transactional
    public void deletePost(@PathVariable Long postId){

        postRepository.deleteById(postId);

    }

}
