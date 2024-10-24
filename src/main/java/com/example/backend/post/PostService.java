package com.example.backend.post;


import com.example.backend.comment.CommentEntity;
import com.example.backend.comment.CommentRepository;
import com.example.backend.comment.CommentRequestDto;
import com.example.backend.comment.CommentResponseDto;
import com.example.backend.user.UserEntity;
import com.example.backend.user.UserRepository;
import com.example.backend.utils.S3Util;
import com.example.backend.utils.VoiceUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
@Log4j2
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository CommentRepository;
    private final UserRepository userRepository;
    private final S3Util s3Util;
    private final VoiceUtil voiceUtil;

    // 게시글 작성
    @Transactional
    public Long createPost(PostWriteDto postWriteDto, MultipartFile thumbnailUrl) {

        // s3
        String directory = "images";
        String s3ThumbUrl = s3Util.upload(directory, thumbnailUrl);

        // user 테이블에서 user_voice_select, user_voice_id, user_name 값 가져오기
        UserEntity user = userRepository.findById(postWriteDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String userVoiceSelect = user.getUserVoiceSelect();
        String userVoiceId = user.getUserVoiceId();
        String userName = user.getUserName(); // userName 가져오기

        // 음성 생성 및 URL 반환

        String s3Url = null;
        if ("ME".equals(userVoiceSelect)) {
            s3Url = voiceUtil.generateVoice(postWriteDto.getPlainText(), userVoiceId);
        } else if ("WOMAN".equals(userVoiceSelect)) {
            s3Url = voiceUtil.generateVoice(postWriteDto.getPlainText(), "uyVNoMrnUku1dZyVEXwD");
        } else if ("MAN".equals(userVoiceSelect)) {
            s3Url = voiceUtil.generateVoice(postWriteDto.getPlainText(), "ZJCNdZEjYwkOElxugmW2");
        }

        // db 값 넣기
        PostEntity postEntity = PostEntity.builder()
                .userId(postWriteDto.getUserId())
                .userName(userName) // userName 등록
                .postTitle(postWriteDto.getPostTitle())
                .postCategory(postWriteDto.getPostCategory())
                .thumbnailUrl(s3ThumbUrl)
                .postSummary(postWriteDto.getPostSummary())
                .postContent(postWriteDto.getPostContent())
                .audioUrl(s3Url)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PostEntity savedPost = postRepository.save(postEntity);

        return savedPost.getPostId();
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
                .postContent(post.getPostContent())
                .isDeleted(post.getIsDeleted())
                .createdAt(post.getCreatedAt())
                .userName(post.getUserName())
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
                        .userName(post.getUserName())
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
                .userName(post.getUserName())
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


    //게시글 조회
    @Transactional
    public List<PostsDto> getAllPosts(){
        List<PostEntity> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> {
                    // 사용자 정보를 가져오기
                    UserEntity user = userRepository.findById(post.getUserId()).orElse(null);
                    String userName = (user != null) ? user.getUserName() : null; // 닉네임을 가져오기

//                    System.out.println("!!!!!!!!!!!!!!!!1Post ID: " + post.getPostId() + ", User Name: " + userName);
                    return PostsDto.builder()
                            .postId(post.getPostId())
                            .userId(post.getUserId())
                            .userName(userName) // 닉네임 추가
                            .postTitle(post.getPostTitle())
                            .postCategory(post.getPostCategory())
                            .thumbnailUrl(post.getThumbnailUrl())
                            .postSummary(post.getPostSummary())
                            .postContent(post.getPostContent())
                            .audioUrl(post.getAudioUrl())
                            .isDeleted(post.getIsDeleted())  // 삭제 여부 추가
                            .createdAt(post.getCreatedAt())
                            .updatedAt(post.getUpdatedAt())  // 업데이트 시간 추가
                            .build();
                })
                .collect(Collectors.toList());
    }

    //카테고리별 게시글 조회
    @Transactional
    public List<PostsDto> getPostsByCategory(String category){
        List<PostEntity> posts = postRepository.findBypostCategory(category);

        return posts.stream()
                .map(post -> {
                    // 사용자 정보를 가져오기
                    UserEntity user = userRepository.findById(post.getUserId()).orElse(null);
                    String userName = (user != null) ? user.getUserName() : null; // 닉네임을 가져오기

                    System.out.println("!!!!!!!!!!!!!!!!1Post ID: " + post.getPostId() + ", User Name: " + userName);
                    return PostsDto.builder()
                            .postId(post.getPostId())
                            .userId(post.getUserId())
                            .userName(userName) // 닉네임 추가
                            .postTitle(post.getPostTitle())
                            .postCategory(post.getPostCategory())
                            .thumbnailUrl(post.getThumbnailUrl())
                            .postSummary(post.getPostSummary())
                            .postContent(post.getPostContent())
                            .audioUrl(post.getAudioUrl())
                            .isDeleted(post.getIsDeleted())  // 삭제 여부 추가
                            .createdAt(post.getCreatedAt())
                            .updatedAt(post.getUpdatedAt())  // 업데이트 시간 추가
                            .build();
                })
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
