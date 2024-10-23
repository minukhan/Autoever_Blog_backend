package com.example.backend.post;


import com.example.backend.comment.CommentRequestDto;
import com.example.backend.comment.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody PostDto postDto) {
        Long postId = postService.createPost(postDto);
        return ResponseEntity.ok(postId);
    }

    @GetMapping
    public List<PostDto> getAllPosts(){
        return postService.getAllPosts();
    }

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

    @PutMapping("/{postId}")
    public PostDto updatePost(@PathVariable Long postId, @RequestBody PostDto postDto) {
        return postService.updatePost(postId, postDto);
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return postId + " 삭제 완료 " ;
    }
    //카테고리별 조회
    @GetMapping("/category/{category}")
    public List<PostDto> getPostsByCategory(@PathVariable String category){
        return postService.getPostsByCategory(category);
    }

    //댓글 등록
    @PostMapping("/{postId}/comments")
    public CommentResponseDto createComment(
            @PathVariable Long postId, // postId를 PathVariable로 받음
            @RequestBody CommentRequestDto requestDto) {

        // postId와 requestDto를 사용하여 댓글을 생성
        return postService.createComment(postId, requestDto);
    }



}
