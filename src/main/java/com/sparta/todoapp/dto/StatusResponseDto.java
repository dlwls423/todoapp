package com.sparta.todoapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
public class StatusResponseDto {

    private final Error error;

    public StatusResponseDto(int statusCode, String msg){
        this.error = new Error(statusCode, msg);
    }

    record Error(int status, String msg){}

}
