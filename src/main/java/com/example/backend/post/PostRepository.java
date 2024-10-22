package com.example.backend.post;

import com.example.backend.domain.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity,Long> {

    List<PostEntity> findByUserId(Long userId);
}
