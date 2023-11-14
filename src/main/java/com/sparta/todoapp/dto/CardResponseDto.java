package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Card;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CardResponseDto {
    private Long cardId;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private String username;

    public CardResponseDto(Card card){
        this.cardId = card.getCardId();
        this.title = card.getTitle();
        this.content = card.getContent();
        this.createdAt = card.getCreatedAt();
        this.username = card.getUser().getUsername();
    }
}
