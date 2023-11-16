package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Card;
import java.time.LocalDateTime;
import lombok.Getter;


@Getter
public class CardInListResponseDto {

    private Long cardId;

    private String title;

    private LocalDateTime createdAt;

    private String username;

    public CardInListResponseDto(Card card){
        this.cardId = card.getCardId();
        this.title = card.getTitle();
        this.createdAt = card.getCreatedAt();
        this.username = card.getUser().getUsername();
    }
}
