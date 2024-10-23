package com.example.backend.playlist;

import com.example.backend.domain.PlaylistEntity;
import com.example.backend.domain.PostEntity;
import com.example.backend.domain.UserEntity;
import com.example.backend.post.PostRepository;
import com.example.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PlayListService {

    private final PlayListRepository playlistRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PlayListDto savePostAsPlaylist(Long userId, Long postId, PlayListDto playListDto) {
        // 사용자 정보를 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));

        // 게시글 정보를 조회
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시글을 찾을 수 없습니다."));

        // 플레이리스트 생성 (Builder 패턴 사용)
        PlaylistEntity playlist = PlaylistEntity.builder()
                .userId(userId)  // 사용자 ID 설정
                .postId(postId)  // 게시글 ID 설정
                .thumbnailUrl(playListDto.getThumbnailUrl())  // 썸네일 URL 설정
                .audioUrl(playListDto.getAudioUrl())  // 오디오 URL 설정
                .createdAt(LocalDateTime.now())  // 생성 시간 설정
                .build();

        // 플레이리스트 저장
        PlaylistEntity savedPlaylist = playlistRepository.save(playlist);

        // PlayListDto로 변환 후 반환
        return PlayListDto.builder()
                .userId(savedPlaylist.getUserId())  // 저장된 User ID
                .postId(savedPlaylist.getPostId())  // 저장된 Post ID
                .playlistId(savedPlaylist.getPlaylistId())  // 저장된 Playlist ID
                .thumbnailUrl(savedPlaylist.getThumbnailUrl())  // 저장된 썸네일 URL
                .audioUrl(savedPlaylist.getAudioUrl())  // 저장된 오디오 URL
                .build();
    }

    @Transactional
    public List<PlayListDto> getAllPlaylist(@PathVariable Long userId){
        List<PlaylistEntity> playlists = playlistRepository.findByUserId(userId);
        if (playlists.isEmpty()) {
            throw new IllegalArgumentException("해당 ID의 사용자는 플레이리스트가 없습니다.");
        }

        return playlists.stream()
                .map(playlist -> PlayListDto.builder()
                        .playlistId(playlist.getPlaylistId())
                        .userId(playlist.getUserId())
                        .postId(playlist.getPostId())
                        .thumbnailUrl(playlist.getThumbnailUrl())
                        .audioUrl(playlist.getAudioUrl())
                        .build())
                .collect(Collectors.toList());
    }
    @Transactional
    public void deletePlaylist(Long userId, Long playListId){
        PlaylistEntity playlist = playlistRepository.findByUserIdAndPlaylistId(userId,playListId)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당 플레이리스트를 찾을 수 없습니다."));
        playlistRepository.delete(playlist);
    }

}