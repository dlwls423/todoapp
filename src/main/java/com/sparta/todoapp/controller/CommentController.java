package com.sparta.todoapp.controller;

import com.sparta.todoapp.controller.exception.CardNotFoundException;
import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.dto.StatusResponseDto;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards/{cardId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
        @PathVariable Long cardId,
        @RequestBody CommentRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        CommentResponseDto responseDto = commentService.createComment(cardId, requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<StatusResponseDto> CardNotFoundExceptionHandler(CardNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new StatusResponseDto(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
            )
        );
    }
}
