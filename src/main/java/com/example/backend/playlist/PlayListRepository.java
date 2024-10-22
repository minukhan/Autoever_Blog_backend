package com.example.backend.playlist;

import com.example.backend.domain.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayListRepository extends JpaRepository<PlaylistEntity,Long> {
}
