package com.example.backend.playlist;

import java.time.LocalDateTime;

public class PlayListDto {

    private Long playlist_id;

    private Long user_id;
    private Long post_id;
    private String thumbnail_url;
    private String audio_url;
    private LocalDateTime createdAt;

}
