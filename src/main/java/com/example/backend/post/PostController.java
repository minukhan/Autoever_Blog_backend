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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Data
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Long> createPost(
            @ModelAttribute PostWriteDto postWriteDto,
            @RequestParam("thumbnailUrl") MultipartFile thumbnailUrl // 이미지 파일을 받을 때 사용
    ) {

        Long postId = postService.createPost(postWriteDto, thumbnailUrl);

        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }


    @GetMapping
    public List<PostsDto> getAllPosts(){
        return postService.getAllPosts();
    }

    //카테고리별 조회
    @GetMapping("/category/{category}")
    public List<PostsDto> getPostsByCategory(@PathVariable String category){
        List<PostsDto> posts = postService.getPostsByCategory(category);
        log.info("######################" + posts.toString());
        return postService.getPostsByCategory(category);
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

    @GetMapping("/user/category/{userId}")
    public List<PostDto> getUserPostsByCategory(@PathVariable Long userId, @RequestParam String category){
        return postService.getUserPosts(userId).stream().filter(post -> post.getPostCategory().equals(category)).collect(Collectors.toList());
    }


    @PutMapping("/{postId}")
    public PostDto updatePost(
            @PathVariable Long postId,
            @ModelAttribute PostWriteDto postWriteDto,
            @RequestParam("thumbnailUrl") MultipartFile thumbnailUrl) {
        return postService.updatePost(postId, postWriteDto, thumbnailUrl);
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return postId + " 삭제 완료 " ;
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
