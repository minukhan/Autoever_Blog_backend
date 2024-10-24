package com.example.backend.authentication;

import com.example.backend.authentication.security.JwtTokenProvider;
import com.example.backend.user.UserEntity;
import com.example.backend.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class AuthController {
    @Value("${kakao.client.id}")
    private String clientId;
    @Value("${kakao.client.secret}")
    private String clientSecret;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthController(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/api/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) {
        String tokenRequestUrl = "https://kauth.kakao.com/oauth/token";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", "http://localhost:5173/authMiddle");
        params.add("code", code);
        params.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        // Kakao 서버에 액세스 토큰 요청
        ResponseEntity<Map> tokenResponse  = restTemplate.postForEntity(tokenRequestUrl, tokenRequest, Map.class);
        Map<String, Object> tokenResponseBody = tokenResponse.getBody();

        if (tokenResponseBody == null || tokenResponseBody.get("access_token") == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve access token from Kakao.");
        }

        String accessToken = (String) tokenResponseBody.get("access_token");  // 액세스 토큰
        String refreshToken = (String) tokenResponseBody.get("refresh_token");  // 리프레시 토큰
        System.out.println("accessToken = " + accessToken);

        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);

        HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, Map.class);

        Map<String, Object> userInfo = userInfoResponse.getBody();
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve user info from Kakao.");
        }

        String kakaoId = String.valueOf(userInfo.get("id"));  // 카카오 사용자 고유 ID
        Map<String, Object> properties = (Map<String, Object>) userInfo.get("properties");
        String nickname = (String) properties.get("nickname");
        String profileImage = (String) properties.get("profile_image");
        System.out.println("kakaoId = " + kakaoId);
        System.out.println("nickname = " + nickname);
        System.out.println("profileImage = " + profileImage);

        UserEntity user = userService.findUserByKakaoId(kakaoId);

        if (user == null) {
            // 처음 로그인하는 사용자 등록
            userService.registerUser(new AuthUserDto(kakaoId, nickname, profileImage, refreshToken)); // 필요한 필드 전달
        } else {
            userService.updateExistingUser(user, new AuthUserDto(kakaoId, nickname, profileImage, refreshToken));
        }

        // 예: 클라이언트에 JWT 반환 또는 로그인 후 처리
        String jwtToken = jwtTokenProvider.createToken(kakaoId, nickname);
        return ResponseEntity.ok(Map.of("token", jwtToken, "userId", kakaoId, "userNickname", nickname, "userProfileImage", profileImage));
    }
}
