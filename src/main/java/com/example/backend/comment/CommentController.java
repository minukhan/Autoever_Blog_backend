package com.example.backend.comment;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@Data
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
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
