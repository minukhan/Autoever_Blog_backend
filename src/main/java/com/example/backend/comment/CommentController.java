package com.example.backend.comment;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //댓글 등록
    @PostMapping("/{postId}/comments")
    public CommentResponseDto createComment(
            @PathVariable Long postId, // postId를 PathVariable로 받음
            @RequestBody CommentRequestDto requestDto) {

        // postId와 requestDto를 사용하여 댓글을 생성
        return commentService.createComment(postId, requestDto);
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @PathVariable Long commentId

            ) {
         commentService.deleteComment(commentId);
    }

    //댓글 게시글 별 조회
    @GetMapping("{postId}")
    public List<CommentResponseDto> getPostById(@PathVariable Long postId){
        return commentService.getPostById(postId);
    }


}
