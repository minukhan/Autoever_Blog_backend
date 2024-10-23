package com.example.backend.post;


import com.example.backend.user.UserEntity;
import com.example.backend.user.UserRepository;
import com.example.backend.utils.S3Util;
import com.example.backend.utils.VoiceUtil;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
@Log4j2
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Util s3Util;
    private final VoiceUtil voiceUtil;

    @Transactional
    public Long createPost(PostWriteDto postWriteDto, MultipartFile thumbnailUrl) {

        // s3
        String directory = "images";
        String s3ThumbUrl = s3Util.upload(directory, thumbnailUrl);

        // user테이블에서 user_voice_select 값 가져오기
        // user 테이블에서 user_voice_select, user_voice_id 값 가져오기
        UserEntity user = userRepository.findById(postWriteDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String userVoiceSelect = user.getUserVoiceSelect();
        String userVoiceId = user.getUserVoiceId();


        // 음성 생성 및 URL 반환
        String s3Url = null;
        if ("ME".equals(userVoiceSelect)) {
            s3Url = voiceUtil.generateVoice(postWriteDto.getPlainText(), userVoiceId);
        } else if ("WOMAN".equals(userVoiceSelect)) {
            s3Url = voiceUtil.generateVoice(postWriteDto.getPlainText(), "uyVNoMrnUku1dZyVEXwD");
        } else if ("MAN".equals(userVoiceSelect)) {
            s3Url = voiceUtil.generateVoice(postWriteDto.getPlainText(), "ZJCNdZEjYwkOElxugmW2");
        }

//        log.info("#########################################s3 url: " + s3Url);

        // db 값 넣기
        PostEntity postEntity = PostEntity.builder()
                .userId(postWriteDto.getUserId())
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


}