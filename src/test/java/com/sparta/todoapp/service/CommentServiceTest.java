package com.sparta.todoapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.sparta.todoapp.controller.exception.AuthorizeException;
import com.sparta.todoapp.controller.exception.BadAccessToCardException;
import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
import com.sparta.todoapp.repository.CommentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Test
    @DisplayName("댓글 생성")
    void test1() {
        // given
        CommentService commentService = new CommentService(commentRepository, cardService);
        CommentRequestDto requestDto = new CommentRequestDto();
        User user = new User("lucy", "1234");
        Card card = new Card("제목", "내용", false, false, user);
        Comment comment = new Comment(requestDto, user, card);
        comment.updateContent("댓글 내용");
        CommentResponseDto responseDto = new CommentResponseDto(comment);
        given(cardService.getCardEntity(1L)).willReturn(card);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // when
        CommentResponseDto resultResponseDto = commentService.createComment(1L, requestDto, user);

        // then
        assertEquals(responseDto.getContent(), resultResponseDto.getContent());

    }

    @Test
    @DisplayName("특정 카드의 댓글 조회")
    void test2() {
        // given
        CommentService commentService = new CommentService(commentRepository, cardService);
        User user = new User("lucy", "1234");
        Card card = new Card("제목", "내용", false, false, user);
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
        CommentService commentService = new CommentService(commentRepository, cardService);
        User user = new User("lucy", "1234");
        Card card = new Card("제목", "내용", false, false, user);
        Comment comment = new Comment("댓글", user, card);
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("댓글 수정");

        given(cardService.getCardEntity(1L)).willReturn(card);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        // when
        CommentResponseDto responseDto = commentService.updateComment(1L, 1L, requestDto, user);

        // then
        assertEquals("댓글 수정", responseDto.getContent());
    }

    @Test
    @DisplayName("댓글 수정 - 해당 카드의 댓글이 아닌 경우")
    void test4() {
        // given
        CommentService commentService = new CommentService(commentRepository, cardService);
        User user = new User("lucy", "1234");
        Card card1 = new Card("제목", "내용", false, false, user);
        Card card2 = new Card("제목", "내용", false, false, user);
        Comment comment = new Comment("댓글", user, card2);
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("댓글 수정");

        given(cardService.getCardEntity(1L)).willReturn(card1);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        // when
        AuthorizeException exception = assertThrows(AuthorizeException.class, () ->
            commentService.updateComment(1L, 1L, requestDto, user)
        );

        // then
        assertEquals("해당 할일카드의 댓글이 아닙니다.", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 수정 - 작성자가 아닌 경우")
    void test5() {
        // given
        CommentService commentService = new CommentService(commentRepository, cardService);
        User user1 = new User("lucy1", "1234");
        User user2 = new User("lucy2", "1234");
        Card card = new Card("제목", "내용", false, false, user1);
        Comment comment = new Comment("댓글", user1, card);
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("댓글 수정");

        given(cardService.getCardEntity(1L)).willReturn(card);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        // when
        AuthorizeException exception = assertThrows(AuthorizeException.class, () ->
            commentService.updateComment(1L, 1L, requestDto, user2)
        );

        // then
        assertEquals("작성자만 삭제/수정할 수 있습니다.", exception.getMessage());
    }

}