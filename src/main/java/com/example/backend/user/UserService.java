package com.example.backend.user;

import com.example.backend.domain.UserEntity;
import com.example.backend.domain.UserVoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //사용자 상세정보 조회
    public UserDetailDto getUserinfo(Long userId){
        UserEntity user = userRepository.findById(userId).orElseThrow();

        return UserDetailDto.builder()
                .isSuccess(true)
                .message("사용자 상세정보 조회 성공")
                .data(
                        UserDetailDto.Data.builder()
                                .userId(user.getUser_id())
                                .email(user.getUser_email())
                                .profileImg(user.getUser_profile_url())
                                .nickname(user.getUser_nickname())
                                .voice(user.getUser_voice_id())
                                .social(
                                        UserDetailDto.Social.builder()
                                                .github(user.getUser_github())
                                                .instagram(user.getUser_insta())
                                                .twitter(user.getUser_twitter())
                                                .build()
                                )
                                .build()
                )
                .build();
    }

    // 사용자 상세 정보 수정
    public void modifyUserInfo(UserRequestDto userRequestDto){
        UserEntity user = userRepository.findById(userRequestDto.getUserId()).orElseThrow();
        user.setUser_profile_url(userRequestDto.getProfileImg());
        user.setUser_email(userRequestDto.getEmail());
        user.setUser_nickname(userRequestDto.getNickname());
        user.setUser_voice_select(userRequestDto.getVoiceselect());
        userRepository.save(user);
    }

    //사용자 blog 소개 + social link 수정
    public void modifyUserSocial(UserRequestDto userRequestDto){
        UserEntity user = userRepository.findById(userRequestDto.getUserId()).orElseThrow();
        user.setUser_intro(userRequestDto.getIntro());
        user.setUser_github(userRequestDto.getGithub());
        user.setUser_insta(userRequestDto.getInsta());
        user.setUser_twitter(userRequestDto.getTwitter());
        userRepository.save(user);
    }
}
