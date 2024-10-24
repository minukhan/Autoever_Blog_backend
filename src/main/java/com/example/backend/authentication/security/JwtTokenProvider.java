package com.example.backend.authentication.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;  // 비밀 키
    private static final long EXPIRATION_TIME = 3 * 24 * 60 * 60 * 1000L;  // 3일 유효

    public String createToken(String kakaoId, String nickname) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .setSubject(kakaoId)  // 사용자 ID
                .claim("nickname", nickname)  // 사용자 정보 포함
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256)  // HS256으로 서명
                .compact();  // JWT 생성
    }
}
