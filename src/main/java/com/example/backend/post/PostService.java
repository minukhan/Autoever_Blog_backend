package com.example.backend.post;


import com.example.backend.comment.CommentEntity;
import com.example.backend.comment.CommentRepository;
import com.example.backend.comment.CommentRequestDto;
import com.example.backend.comment.CommentResponseDto;
import com.example.backend.user.UserEntity;
import com.example.backend.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final CommentRepository CommentRepository;
    private final UserRepository userRepository;
    // 게시글 작성
    @Transactional
    public Long createPost(PostDto postDto){

        UserEntity user = userRepository.findByUserId(postDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));

        PostEntity postEntity = PostEntity.builder()
                .userId(postDto.getUserId())
                .userName(user.getUserName())
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
                        .userName(post.getUserName())
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
                .userName(post.getUserName())
                .userId(post.getUserId())
                .postTitle(post.getPostTitle())
                .postCategory(post.getPostCategory())
                .postContent(post.getPostContent())
                .audioUrl(post.getAudioUrl())
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
                        .userName(post.getUserName())
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
        postRepository.findById(postId).orElseThrow(() ->
                new EntityNotFoundException("Post with id " + postId + " does not exist"));

        postRepository.deleteById(postId);

    }

    //카테고리별 게시글 조회
    @Transactional
    public List<PostDto> getPostsByCategory(String category){
        List<PostEntity> posts = postRepository.findBypostCategory(category);

        return posts.stream()
                .map(post -> PostDto.builder()
                        .postId(post.getPostId())
                        .userName(post.getUserName())
                        .userId(post.getUserId())
                        .postTitle(post.getPostTitle())
                        .postCategory(post.getPostCategory())
                        .thumbnailUrl(post.getThumbnailUrl())
                        .postSummary(post.getPostSummary())
                        .postContent(post.getPostContent())
                        .audioUrl(post.getAudioUrl())
                        .isDeleted(post.getIsDeleted())  // 삭제 여부 추가
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())  // 업데이트 시간 추가
                        .build())
                .collect(Collectors.toList());
    }

    // 댓글 생성
    @Transactional
    public CommentResponseDto createComment(
            @PathVariable Long postId,
            CommentRequestDto requestDto) {

        //만약 id를 찾지못하면 에러를 출력
        UserEntity user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID"));


        // CommentEntity 생성
        CommentEntity comment = CommentEntity.builder()
                .userId(user.getUserId())
                .postId(postId)// User의 ID를 설정
                .commentContent(requestDto.getCommentContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 댓글 저장
        CommentRepository.save(comment);

        // 저장된 댓글을 CommentResponseDto로 변환 후 반환
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())  // 생성된 댓글 ID
                .postId(postId)                     // 해당 게시물 ID
                .userId(comment.getUserId())        // 댓글 작성자 ID
                .commentContent(comment.getCommentContent())  // 댓글 내용
                .createdAt(comment.getCreatedAt())  // 생성 시간
                .updatedAt(comment.getUpdatedAt())  // 수정 시간
                .build();
    }

}
