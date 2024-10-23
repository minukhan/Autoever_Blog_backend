package com.example.backend.playlist;

import com.example.backend.domain.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayListRepository extends JpaRepository<PlaylistEntity,Long> {
    List<PlaylistEntity> findByUserId(Long userId);
    Optional<PlaylistEntity> findByUserIdAndPlaylistId(Long userId, Long playlistId);
}
