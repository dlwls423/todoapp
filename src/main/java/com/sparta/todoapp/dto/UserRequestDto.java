package com.sparta.todoapp.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserRequestDto {
    @Size(min=4, max=10)
    @Pattern(regexp = "^[a-z0-9]*$")
    private String username;

    @Size(min=8, max=15)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String password;

    public UserRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
