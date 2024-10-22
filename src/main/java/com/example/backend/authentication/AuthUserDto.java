package com.example.backend.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserDto {
    private String kakaoId;
    private String nickname;
    private String profileImage;
    private String refreshToken;
}
