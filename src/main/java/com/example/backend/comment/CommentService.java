package com.example.backend.comment;

import com.example.backend.domain.CommentEntity;
import com.example.backend.domain.PostEntity;
import com.example.backend.domain.UserEntity;
import com.example.backend.post.PostRepository;
import com.example.backend.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    // 댓글 생성
    @Transactional
    public CommentResponseDto createComment(
            @PathVariable Long postId,
            CommentRequestDto requestDto) {

        UserEntity user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));
        //만약 id를 찾지못하면 에러를 출력

        // CommentEntity 생성
        CommentEntity comment = CommentEntity.builder()
                .userId(user.getUserId())
                .postId(postId)// User의 ID를 설정
                .commentContent(requestDto.getCommentContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 댓글 저장
        commentRepository.save(comment);

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

    //댓글 삭제
    public void deleteComment(
            @PathVariable Long commentId
                              ){

        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));
        commentRepository.deleteById(commentId);

    }

    //댓글 게시글 별 조회
    public List<CommentResponseDto> getPostById(@PathVariable Long postId){

        List<CommentEntity> comments = commentRepository.findByPostId(postId);

        // 댓글이 존재하지 않는 경우 예외 처리
        if (comments.isEmpty()) {
            throw new IllegalArgumentException("해당 게시물에 댓글이 없습니다.");
        }

        return comments.stream()
                .map(comment -> CommentResponseDto.builder()
                        .commentId(comment.getCommentId())    // 댓글 ID
                        .postId(comment.getPostId())          // 게시물 ID
                        .userId(comment.getUserId())          // 댓글 작성자 ID
                        .commentContent(comment.getCommentContent()) // 댓글 내용
                        .createdAt(comment.getCreatedAt())    // 댓글 생성 시간
                        .updatedAt(comment.getUpdatedAt())    // 댓글 수정 시간
                        .build())
                .collect(Collectors.toList());
    }
}