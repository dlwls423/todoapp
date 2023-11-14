package com.sparta.todoapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
public class StatusResponseDto {

    private final int statusCode;

    private final String msg;

    public StatusResponseDto(int statusCode, String msg){
        this.statusCode = statusCode;
        this.msg = msg;
    }


}
