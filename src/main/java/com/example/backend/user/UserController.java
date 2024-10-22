package com.example.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;

    //사용자 상세정보 조회
    @GetMapping("/{userId}")
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
    @PutMapping("/{userId}")
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
        }    }

    //사용자 social link 수정
    @PutMapping("/{userId}/social")
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
}
