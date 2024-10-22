package com.example.backend.post;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;


    @PostMapping
    public ResponseEntity<String> createPost(
            @RequestParam("image") MultipartFile image,
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam("content") String content) {

        // 파일 및 데이터 처리 로직
        // 예를 들어, 파일 저장, 데이터베이스에 저장 등을 수행

        log.info("Title: " + title);
        log.info("Summary: " + summary);
        log.info("Content: " + content);
        log.info("Image Name: " + image.getOriginalFilename());
        String url = postService.createPost(title, summary, content, image);


        // 성공 응답 반환
        return new ResponseEntity<>(url, HttpStatus.CREATED);
    }

//    @PostMapping
//    public ResponseEntity<Long> createPost(@RequestBody PostWriteDto postWriteDto) {
//        System.out.println("###############################" + postWriteDto);
////        Long postId = postService.createPost(postWriteDto);
//
//        return ResponseEntity.ok(postId);
//    }
//
//    @GetMapping
//    public List<PostDto> getAllPosts(){
//        return postService.getAllPosts();
//    }

    // 특정 게시글 조회
    @GetMapping("/{postId}")
    public PostDto getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    //유저 아이디 별 게시글 조회
    @GetMapping("/user/{userId}")
    public List<PostDto> getUserPosts(@PathVariable Long userId){
        return postService.getUserPosts(userId);
    }

}