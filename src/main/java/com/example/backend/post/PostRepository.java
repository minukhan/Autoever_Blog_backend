package com.example.backend.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity,Long> {

    List<PostEntity> findByUserId(Long userId);
    List<PostEntity> findBypostCategory(String category);
    Optional<PostEntity> findById(Long postId);
}
