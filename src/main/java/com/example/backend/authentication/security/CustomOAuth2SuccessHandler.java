package com.example.backend.authentication.security;

import com.example.backend.authentication.AuthUserDto;
import com.example.backend.user.UserEntity;
import com.example.backend.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

//@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public CustomOAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider, UserService userService, OAuth2AuthorizedClientService authorizedClientService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // OAuth2 로그인 후 사용자 정보 가져오기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 사용자 정보 (예: 카카오 ID와 닉네임)
        String kakaoId = oAuth2User.getName();  // 카카오 사용자 고유 ID
        String nickname = (String) ((Map<String, Object>) oAuth2User.getAttributes().get("properties")).get("nickname");  // 닉네임
        String profileImage = (String) ((Map<String, Object>) oAuth2User.getAttributes().get("properties")).get("profile_image");

        // AccessToken 및 RefreshToken 가져오기
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId(); // 클라이언트 ID (예: kakao)

        // OAuth2AuthorizedClientService를 통해 인증된 클라이언트 정보 로드
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                clientRegistrationId,
                oauthToken.getName());

        String accessToken = authorizedClient.getAccessToken().getTokenValue();  // Access Token
        String refreshToken = authorizedClient.getRefreshToken() != null ? authorizedClient.getRefreshToken().getTokenValue() : null;  // Refresh Token

        // JWT 생성
        String jwtToken = jwtTokenProvider.createToken(kakaoId, nickname);

        // 기존 회원인지 확인
        UserEntity user = userService.findUserByKakaoId(kakaoId);

        if (user == null) {
            // 처음 로그인하는 사용자 등록
            userService.registerUser(new AuthUserDto(kakaoId, nickname, profileImage, refreshToken)); // 필요한 필드 전달
        } else {
            userService.updateExistingUser(user, new AuthUserDto(kakaoId, nickname, profileImage, refreshToken));
        }

        // JWT를 JSON으로 클라이언트에 반환
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"token\": \"" + jwtToken + "\", \"profileImage\": \"" + profileImage + "\"}");
    }
}
