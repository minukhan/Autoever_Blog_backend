package com.example.backend.user;

import com.example.backend.authentication.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    public UserController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/api/users/register")
    public ResponseEntity<Object> userInitialSetting(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserInitialDto userInitialDto) {
        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = authService.extractUserId(token);

        userService.initializeUserInfo(userId, userInitialDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
