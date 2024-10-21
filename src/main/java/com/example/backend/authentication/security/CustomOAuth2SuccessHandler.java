package com.example.backend.authentication.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;

    public CustomOAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // OAuth2 로그인 후 사용자 정보 가져오기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 사용자 정보 (예: 카카오 ID와 닉네임)
        String kakaoId = oAuth2User.getName();  // 카카오 사용자 고유 ID
        String nickname = (String) ((Map<String, Object>) oAuth2User.getAttributes().get("properties")).get("nickname");  // 닉네임

        // JWT 생성
        String jwtToken = jwtTokenProvider.createToken(kakaoId, nickname);

        // JWT를 JSON으로 클라이언트에 반환
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"token\": \"" + jwtToken + "\"}");
    }
}
