package com.example.backend.playlist;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/playlists")
@CrossOrigin(origins = "*")
public class PlayListController {

    private final PlayListService playListService;

    @PostMapping("/{userId}")
    public ResponseEntity<PlayListDto> addPostToPlaylist(
            @PathVariable Long userId,
            @RequestParam Long postId,
            @RequestBody PlayListDto playListDto
    ){
        PlayListDto savedPlaylist = playListService.savePostAsPlaylist(userId,postId,playListDto);
        return ResponseEntity.ok(savedPlaylist);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<PlayListDto>> getAllPlaylist(@PathVariable Long userId){
        List<PlayListDto> playList = playListService.getPlayListByUser(userId);
        return ResponseEntity.ok(playList);
    }

    @DeleteMapping("/{userId}/{playListId}")
    public ResponseEntity<Void> deletePlaylist(
            @PathVariable Long userId,
            @PathVariable Long playListId
    ){
        playListService.deletePlaylist(userId,playListId);
        return ResponseEntity.noContent().build();
    }


}
