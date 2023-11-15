package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long commentId;

    private String content;

    private String username;

    private Long cardId;

    public CommentResponseDto(Comment saveComment) {
        this.commentId = saveComment.getCommmentId();
        this.content = saveComment.getContent();
        this.username = saveComment.getUser().getUsername();
        this.cardId = saveComment.getCard().getCardId();
    }
}
