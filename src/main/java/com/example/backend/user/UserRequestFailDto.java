package com.example.backend.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestFailDto {
    private int status;
    private String message;
    private HttpStatus error;
    private String path;
    private Timestamp timestamp;

    @Builder(builderMethodName = "withAll")
    public UserRequestFailDto(HttpStatus error, String message, String path){
        this.status = error.value();
        this.message = message;
        this.error = error;
        this.path = path;

        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
    }
}
