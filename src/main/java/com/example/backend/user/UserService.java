package com.example.backend.user;
import org.springframework.stereotype.Service;
import com.example.backend.authentication.AuthUserDto;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //사용자 상세정보 조회
    public UserDetailDto getUserinfo(Long userId){
        UserEntity user = userRepository.findById(userId).orElseThrow();
        System.out.println(UserVoice.valueOf(user.getUserVoiceSelect()));
        return UserDetailDto.builder()
                .isSuccess(true)
                .message("사용자 상세정보 조회 성공")
                .data(
                        UserDetailDto.Data.builder()
                                .userId(user.getUserId())
                                .name(user.getUserName())
                                .voice(UserVoice.valueOf(user.getUserVoiceSelect()))
                                .social(
                                        UserDetailDto.Social.builder()
                                                .github(user.getUserGithub())
                                                .instagram(user.getUserInsta())
                                                .twitter(user.getUserTwitter())
                                                .build()
                                )
                                .build()
                )
                .build();
    }

    // 사용자 상세 정보 수정
    //사용자 프로필 수정 불가 -> 닉네임, 선택 보이스 변경만
    public void modifyUserInfo(UserRequestDto userRequestDto){
        UserEntity user = userRepository.findById(userRequestDto.getUserId()).orElseThrow();
        user.changeUserName(userRequestDto.getName());
        user.changeUserVoiceSelect(userRequestDto.getVoiceSelect().toString());
        userRepository.save(user);
    }

    //사용자 blog 소개 + social link 수정
    public void modifyUserSocial(UserRequestDto userRequestDto){
        UserEntity user = userRepository.findById(userRequestDto.getUserId()).orElseThrow();
        user.changeUserIntro(userRequestDto.getIntro());
        user.changeUserGithub(userRequestDto.getGithub());
        user.changeUserInsta(userRequestDto.getInsta());
        user.changeUserTwitter(userRequestDto.getTwitter());
        userRepository.save(user);
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
}
