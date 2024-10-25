package com.example.backend.user;

import com.example.backend.authentication.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    UserService userService;
    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }


    //사용자 회원탈퇴
    @DeleteMapping("/api/users/{userId}/delete")
    public ResponseEntity<?> deleteUserInfo(
            @PathVariable(name = "userId") Long userId
    ){
//        try{
//            userService.changeToDeletedUser(userId);
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(Map.of("isSuccess", true, "message", "사용자 탈퇴 성공"));
//        }catch(Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                    UserRequestFailDto.withAll()
//                            .error(HttpStatus.INTERNAL_SERVER_ERROR)
//                            .message(e.getMessage())
//                            .path("/api/users/"+userId+"/delete")
//                            .build()
//            );
//        }
        userService.deleteUserAndPosts(userId);
        return ResponseEntity.ok("User and posts deleted successfully.");
    }

    //사용자 상세정보 조회
    @GetMapping("/api/users/{userId}")
    public ResponseEntity<?> getUserInfo(
            @PathVariable(name = "userId") Long userId
    ){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserinfo(userId));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    UserRequestFailDto.withAll()
                            .error(HttpStatus.INTERNAL_SERVER_ERROR)
                            .message(e.getMessage())
                            .path("/api/users/"+userId)
                            .build()
            );
        }
    }

    //사용자 정보 수정
    @PutMapping("/api/users/{userId}")
    public ResponseEntity<?> editUserInfo(
            @PathVariable(name = "userId") Long userId,
            @RequestBody UserRequestDto userRequestDto
    ){
        try{
            userRequestDto.setUserId(userId);
            userService.modifyUserInfo(userRequestDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("isSuccess", true, "message", "성공"));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    UserRequestFailDto.withAll()
                            .error(HttpStatus.INTERNAL_SERVER_ERROR)
                            .message(e.getMessage())
                            .path("/api/users/"+userId)
                            .build()
            );
        }
    }

    //사용자 social link 수정
    @PutMapping("/api/users/{userId}/social")
    public ResponseEntity<?> editUserSocial(
            @PathVariable(name = "userId") Long userId,
            @RequestBody UserRequestDto userRequestDto
    ){
        try{
            userRequestDto.setUserId(userId);
            userService.modifyUserSocial(userRequestDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("isSuccess", true, "message", "성공"));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    UserRequestFailDto.withAll()
                            .error(HttpStatus.INTERNAL_SERVER_ERROR)
                            .message(e.getMessage())
                            .path("/api/users/"+userId)
                            .build()
            );
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/users/register/{userId}")
    public ResponseEntity<Object> userInitialSetting(@PathVariable Long userId, @RequestBody UserInitialDto userInitialDto) {

        userService.initializeUserInfo(userId, userInitialDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
