package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.StatusResponseDto;
import com.sparta.todoapp.dto.UserRequestDto;
import com.sparta.todoapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController extends ExceptionHandler {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<StatusResponseDto> signup(@RequestBody @Valid UserRequestDto requestDto){

        userService.signup(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            new StatusResponseDto(
                HttpStatus.CREATED.value(),
                "회원가입 성공"
            )
        );
    }

}
