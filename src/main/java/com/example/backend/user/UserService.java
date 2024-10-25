package com.example.backend.user;
import com.example.backend.post.PostEntity;
import com.example.backend.post.PostRepository;
import com.example.backend.utils.VoiceUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.backend.authentication.AuthUserDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final VoiceUtil voiceUtil;

    public UserService(UserRepository userRepository, PostRepository postRepository, VoiceUtil voiceUtil) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.voiceUtil = voiceUtil;
    }
    //사용자 상세정보 조회 (프로필 이미지 추가)
    public UserDetailDto getUserinfo(Long userId){
        System.out.println(userId);
        UserEntity user = userRepository.findById(userId).orElseThrow();

        return UserDetailDto.builder()
                .isSuccess(true)
                .message("사용자 상세정보 조회 성공")
                .data(
                        UserDetailDto.Data.builder()
                                .userId(user.getUserId())
                                .name(user.getUserName())
                                .voice(user.getUserVoiceSelect() == null ? UserVoice.MAN : UserVoice.valueOf(user.getUserVoiceSelect()))
                                .profileImg(user.getUserProfileImage())
                                .social(
                                        UserDetailDto.Social.builder()
                                                .intro(user.getUserIntro())
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
        user.changeUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        List<PostEntity> posts = postRepository.findByUserId(userRequestDto.getUserId());
        if(posts.isEmpty()) {
            return;
        }
        else{
            String plainText;
            String userVoiceSelect = user.getUserVoiceSelect();
            String userVoiceId = user.getUserVoiceId();
            String userName = user.getUserName();

            String s3Url = null;
            for(PostEntity p : posts){
                plainText = p.getPostContent().replaceAll("<[^>]*>", "");
                s3Url = null;

                if ("ME".equals(userVoiceSelect)) {
                    s3Url = voiceUtil.generateVoice(plainText, userVoiceId);
                } else if ("WOMAN".equals(userVoiceSelect)) {
                    s3Url = voiceUtil.generateVoice(plainText, "uyVNoMrnUku1dZyVEXwD");
                } else if ("MAN".equals(userVoiceSelect)) {
                    s3Url = voiceUtil.generateVoice(plainText, "ZJCNdZEjYwkOElxugmW2");
                }

                p.setAudioUrl(s3Url);
                postRepository.save(p);
            }

            return;
        }

    }

    //사용자 blog 소개 + social link 수정
    public void modifyUserSocial(UserRequestDto userRequestDto){
        UserEntity user = userRepository.findById(userRequestDto.getUserId()).orElseThrow();
        user.changeUserIntro(userRequestDto.getIntro());
        user.changeUserGithub(userRequestDto.getGithub());
        user.changeUserInsta(userRequestDto.getInsta());
        user.changeUserTwitter(userRequestDto.getTwitter());
        user.changeUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    // 회원 탈퇴
    public void changeToDeletedUser(Long userId){
        UserEntity user = userRepository.findById(userId).orElseThrow();
        user.changeDeleted(true);
        user.changeDeletedAt(LocalDateTime.now());
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
                .userIntro(authUserDto.getNickname()+"님의 블로그 ^_^")
                .userVoiceId("ZJCNdZEjYwkOElxugmW2")
                .userVoiceSelect("MAN")
                .userGithub("https://github.com/kingnuna")
                .userInsta("https://www.instagram.com/zuck/")
                .userTwitter("https://x.com/elonmusk")
                .createdAt(LocalDateTime.now().withNano(0))
                .updatedAt(LocalDateTime.now().withNano(0))
                .isDeleted(Boolean.FALSE)
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
        userEntity.changeUserVoiceSelect(userInitialDto.getVoiceSelected());
        userRepository.save(userEntity);
    }

    @Transactional
    public void deleteUserAndPosts(Long userId) {
        // 유저의 게시글 삭제
        postRepository.deleteByUserId(userId);

        // 유저 삭제
        userRepository.deleteById(userId);
    }
}
