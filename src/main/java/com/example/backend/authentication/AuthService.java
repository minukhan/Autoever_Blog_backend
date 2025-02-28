package com.example.backend.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthService {

    @Value("${jwt.secret-key}")
    private String secretKey;
    private final RestTemplate restTemplate = new RestTemplate();

    public String requestAccessToken(String code) {
        String tokenRequestUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "YOUR_CLIENT_ID");
        params.add("redirect_uri", "YOUR_BACKEND_REDIRECT_URI");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenRequestUrl, request, Map.class);
        Map<String, Object> kakaoTokenResponse = response.getBody();

        return (String) kakaoTokenResponse.get("access_token");
    }



    // JWT 토큰에서 userId 추출
    public Long extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())  // 서명 검증을 위한 키
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.getSubject());  // `sub` 필드에서 userId 추출
    }
}
