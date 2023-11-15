package com.sparta.todoapp.service;

import com.sparta.todoapp.controller.exception.AuthorizeException;
import com.sparta.todoapp.controller.exception.EntityNotFoundException;
import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
import com.sparta.todoapp.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public CommentResponseDto updateComment(Long cardId, Long commentId, CommentRequestDto requestDto, User user) {
        Card card = getCardEntity(cardId);
        Comment comment = getCommentEntity(commentId);
        checkCard(comment, card);
        checkUser(comment, user);
        comment.updateContent(requestDto.getContent());
        return new CommentResponseDto(comment);
    }

    private Card getCardEntity(Long cardId){
        Card card = cardRepository.findById(cardId).orElseThrow(
            () -> new EntityNotFoundException("해당 카드를 찾을 수 없습니다.")
        );
        return card;
    }

    private Comment getCommentEntity(Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow(
            () -> new EntityNotFoundException("해당 댓글를 찾을 수 없습니다.")
        );
        return comment;
    }

    private void checkCard(Comment comment, Card card){
        if(!comment.getCard().equals(card)){
            throw new AuthorizeException("해당 할일카드의 댓글이 아닙니다.");
        }
    }

    private void checkUser(Comment comment, User user){
        if(!comment.getUser().getUsername().equals(user.getUsername())){
            throw new AuthorizeException("권한이 없습니다.");
        }
    }
}
