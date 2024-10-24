package com.example.backend.voice;

import lombok.Data;

@Data
public class VoiceIdResponse {
    private String voice_id; // API의 voice_id
    private boolean requires_verification; // API의 requires_verification
}
