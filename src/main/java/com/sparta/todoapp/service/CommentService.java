package com.sparta.todoapp.service;

import com.sparta.todoapp.controller.exception.CardNotFoundException;
import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
import com.sparta.todoapp.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final CardRepository cardRepository;

    public CommentResponseDto createComment(Long cardId, CommentRequestDto requestDto, User user) {
        Card card = getCardEntity(cardId);
        Comment comment = new Comment(requestDto, user, card);
        Comment saveComment = commentRepository.save(comment);
        return new CommentResponseDto(saveComment);
    }

    public Card getCardEntity(Long cardId){
        Card card = cardRepository.findById(cardId).orElseThrow(
            () -> new CardNotFoundException("해당 카드를 찾을 수 없습니다.")
        );
        return card;
    }

}
