package com.sparta.todoapp.dto;

import lombok.Getter;

@Getter
public class CardRequestDto {
    private String title;

    private String content;

    private boolean privateCard;

    public CardRequestDto(String title, String content, boolean privateCard) {
        this.title = title;
        this.content = content;
        this.privateCard = privateCard;
    }
}
