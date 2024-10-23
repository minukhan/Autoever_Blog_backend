package com.example.backend.voice;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/voice")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin(origins = "*")
public class VoiceController {
    private final VoiceService voiceService;

    @PostMapping("/{userId}")
    public ResponseEntity<String> createVoice(
            @PathVariable Long userId,
            @RequestParam("voiceUrl") MultipartFile[] voiceUrl
    ) throws IOException {

        String voiceId = voiceService.createVoice(userId, voiceUrl);
        log.info("#######################Voice created##################: " + voiceId);
        return ResponseEntity.ok(voiceId);
    }
}
