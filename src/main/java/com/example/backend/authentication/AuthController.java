package com.example.backend.authentication;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AuthController {

    @GetMapping("/api/users/login")
    public void redirectToKakaoLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/kakao"); // 카카오 OAuth2 인증 요청 경로로 리디렉션
    }
}
