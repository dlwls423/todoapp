package com.sparta.todoapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.sparta.todoapp.controller.exception.AuthorizeException;
import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CommentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;

    @Mock
    CardService cardService;

    CommentService commentService;

    User user;
    Card card;
    CommentRequestDto requestDto;
    Comment comment;

    @BeforeEach
    void setup() {
        commentService = new CommentService(commentRepository, cardService);
        user = new User("lucy", "1234");
        card = new Card("제목", "내용", false, false, user);
        requestDto = new CommentRequestDto("내용");
        comment = new Comment(requestDto, user, card);
    }

    @Test
    @DisplayName("댓글 생성")
    void test1() {
        // given
        given(cardService.getCardEntity(1L)).willReturn(card);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // when
        CommentResponseDto resultResponseDto = commentService.createComment(1L, requestDto, user);

        // then
        assertEquals(requestDto.getContent(), resultResponseDto.getContent());

    }

    @Test
    @DisplayName("특정 카드의 댓글 조회")
    void test2() {
        // given
        Comment comment1 = new Comment("댓글1", user, card);
        Comment comment2 = new Comment("댓글2", user, card);
        Comment comment3 = new Comment("댓글3", user, card);
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment1);
        commentList.add(comment2);
        commentList.add(comment3);

        given(cardService.getCardEntity(1L)).willReturn(card);
        given(commentRepository.findAllByCard(card)).willReturn(commentList);

        // when
        List<CommentResponseDto> responseDtoList = commentService.getComments(1L);

        // then
        assertEquals(3, responseDtoList.size());
    }

    @Test
    @DisplayName("댓글 수정")
    void test3() {
        // given
        CommentRequestDto updateRequestDto = new CommentRequestDto("댓글 수정");

        given(cardService.getCardEntity(1L)).willReturn(card);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        // when
        CommentResponseDto responseDto = commentService.updateComment(1L, 1L, updateRequestDto, user);

        // then
        assertEquals("댓글 수정", responseDto.getContent());
    }

    @Test
    @DisplayName("댓글 수정 - 해당 카드의 댓글이 아닌 경우")
    void test4() {
        // given
        Card card2 = new Card("제목", "내용", false, false, user);
        Comment comment = new Comment("댓글", user, card2);
        CommentRequestDto updateRequestDto = new CommentRequestDto("댓글 수정");

        given(cardService.getCardEntity(1L)).willReturn(card);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        // when
        AuthorizeException exception = assertThrows(AuthorizeException.class, () ->
            commentService.updateComment(1L, 1L, updateRequestDto, user)
        );

        // then
        assertEquals("해당 할일카드의 댓글이 아닙니다.", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 수정 - 작성자가 아닌 경우")
    void test5() {
        // given
        User user2 = new User("lucy2", "1234");
        CommentRequestDto updateRequestDto = new CommentRequestDto("댓글 수정");

        given(cardService.getCardEntity(1L)).willReturn(card);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        // when
        AuthorizeException exception = assertThrows(AuthorizeException.class, () ->
            commentService.updateComment(1L, 1L, updateRequestDto, user2)
        );

        // then
        assertEquals("작성자만 삭제/수정할 수 있습니다.", exception.getMessage());
    }

}