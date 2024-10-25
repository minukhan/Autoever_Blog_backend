package com.example.backend.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity,Long> {

    List<PostEntity> findByUserId(Long userId);
    List<PostEntity> findBypostCategory(String category);
    Optional<PostEntity> findById(Long postId);

    @Modifying
    @Query("DELETE FROM PostEntity p WHERE p.userId = :userId")
    void deleteByUserId(Long userId);
}
