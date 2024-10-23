package com.example.backend.voice;

import com.example.backend.user.UserEntity;
import com.example.backend.user.UserRepository;
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
    private final UserRepository userRepository;

    public String createVoice(Long userId, MultipartFile[] voiceUrl) throws IOException {
//        String voiceId = voiceUtil.createVoiceId(userId, voiceUrl);
        VoiceIdResponse voiceIdResponse = voiceUtil.createVoiceId(userId, voiceUrl); // DTO 받기

        UserEntity user = userRepository.findById(userId).orElse(null);

        // db에 저장
        if (user != null) {
            // userVoiceId에 voiceId 저장
            user.changeUserVoiceId(voiceIdResponse.getVoice_id());
            userRepository.save(user);
            log.info("###############################User voice ID updated: userId={}, voiceId={}", userId, voiceIdResponse.getVoice_id());

            // s3 url 받기
            String testScript = "이 음성은 회원님의 회원님의 목소리를 AI가 분석해 모델링한 AI 목소리입니다.";

            return voiceUtil.generateVoice(testScript, voiceIdResponse.getVoice_id()); // S3 URL 반환
        } else {
            log.warn("User not found for userId: {}", userId);
            throw new IllegalArgumentException("User not found for userId: " + userId); // 예외 던지기
        }
    }
}
