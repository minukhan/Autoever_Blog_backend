package com.example.backend.post;


import com.example.backend.utils.S3Util;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
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
public class PostService {

    private final PostRepository postRepository;
    private final S3Util s3Util;

    @Transactional
    public String createPost(String title, String summary, String content, MultipartFile image){

        // Repository를 사용하여 게시글 정보를 DB에 저장한다.


        // 저장한 정보에서 postId 정보를 추출하고, 썸네일 이미지를 S3에 저장한다.
        String postId = "1"; // FIXME: postId 가져오게 수정
        String directory = "images/" + postId;
        return s3Util.upload(directory, image);

        // dirty checking을 통해 썸네일 이미지를 저장한다.



        // /images폴더/postId/imageurl


//        PostEntity postEntity = PostEntity.builder()
//                .userId(postWriteDto.getUserId())
//                .postTitle(postWriteDto.getPostTitle())
//                .postCategory(postWriteDto.getPostCategory())
//                .thumbnailUrl(postWriteDto.getThumbnailUrl())
//                .postSummary(postWriteDto.getPostSummary())
//                .postContent(postWriteDto.getPostContent())
////                .audioUrl(postWriteDto.getAudioUrl())
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();

//        PostEntity savedPost = postRepository.save(postEntity);
//        return savedPost.getPostId();
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