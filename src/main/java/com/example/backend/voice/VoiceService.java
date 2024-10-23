package com.example.backend.voice;

import com.example.backend.utils.VoiceUtil;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Builder
@Log4j2
public class VoiceService {

    private final VoiceUtil voiceUtil;

    public String createVoice(Long userId, MultipartFile[] voiceUrl) throws IOException {
        String voiceId = voiceUtil.createVoiceId(userId, voiceUrl);

        return voiceId;
    }
}
