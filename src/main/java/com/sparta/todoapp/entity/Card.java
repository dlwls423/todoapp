package com.sparta.todoapp.entity;

import com.sparta.todoapp.dto.CardRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "cards")
@NoArgsConstructor
public class Card extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private boolean complete = false;

    @Column
    private boolean privateCard = false;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Card(CardRequestDto cardRequestDto, User user) {
        this.title = cardRequestDto.getTitle();
        this.content = cardRequestDto.getContent();
        this.privateCard = cardRequestDto.isPrivateCard();
        this.user = user;
    }

    public void update(CardRequestDto cardRequestDto) {
        this.title = cardRequestDto.getTitle();
        this.content = cardRequestDto.getContent();
        this.privateCard = cardRequestDto.isPrivateCard();
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public Card(String title, String content, boolean complete, boolean privateCard, User user) {
        this.title = title;
        this.content = content;
        this.complete = complete;
        this.privateCard = privateCard;
        this.user = user;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}


