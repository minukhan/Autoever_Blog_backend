package com.example.backend.user;

import com.example.backend.authentication.AuthUserDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 기존 사용자인지 확인하는 메서드
    public UserEntity findUserByKakaoId(String kakaoId) {
        return userRepository.findById(Long.valueOf(kakaoId)).orElse(null);
    }

    // 회원가입 처리
    public void registerUser(AuthUserDto authUserDto) {
        UserEntity user = UserEntity.builder()
                .userId(Long.valueOf(authUserDto.getKakaoId()))
                .userName(authUserDto.getNickname())
                .userProfileImage(authUserDto.getProfileImage())
                .userRefreshToken(authUserDto.getRefreshToken())
                .build();
        userRepository.save(user);
    }

    // 기존 회원 정보 업데이트
    public void updateExistingUser(UserEntity user, AuthUserDto authUserDto) {
        user.changeUserName(authUserDto.getNickname());
        user.changeUserProfileImage(authUserDto.getProfileImage());
        user.changeUserRefreshToken(authUserDto.getRefreshToken());
        userRepository.save(user);
    }

    public void initializeUserInfo(Long userId, UserInitialDto userInitialDto) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        userEntity.changeUserName(userInitialDto.getUserNickname());
        userEntity.changeUserIntro(userInitialDto.getUserDesc());
        userEntity.changeUserVoiceSelect(userInitialDto.getVoice());
        userRepository.save(userEntity);
    }
}
