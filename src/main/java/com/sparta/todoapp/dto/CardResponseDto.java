package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CardResponseDto {

    private Long cardId;

    private String title;

    private String content;

    private boolean complete;

    private LocalDateTime createdAt;

    private String username;

    public CardResponseDto(Card card){
        this.cardId = card.getCardId();
        this.title = card.getTitle();
        this.content = card.getContent();
        this.complete = card.isComplete();
        this.createdAt = card.getCreatedAt();
        this.username = card.getUser().getUsername();
    }
}
