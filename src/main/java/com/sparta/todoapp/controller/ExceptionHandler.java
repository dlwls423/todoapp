package com.sparta.todoapp.controller;

import com.sparta.todoapp.controller.exception.BadAccessToCardException;
import com.sparta.todoapp.controller.exception.AuthorizeException;
import com.sparta.todoapp.controller.exception.EntityNotFoundException;
import com.sparta.todoapp.dto.StatusResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StatusResponseDto> CardNotFoundExceptionHandler(EntityNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new StatusResponseDto(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
            )
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthorizeException.class)
    public ResponseEntity<StatusResponseDto> AuthorizeExceptionHandler(AuthorizeException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            new StatusResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage()
            )
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadAccessToCardException.class)
    public ResponseEntity<StatusResponseDto> BadAccessToCardExceptionHandler(
        BadAccessToCardException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            new StatusResponseDto(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage()
            )
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StatusResponseDto> processValidationError(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for(FieldError fieldError : bindingResult.getFieldErrors()){
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new StatusResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                builder.toString()
            )
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StatusResponseDto> illegalArgumentException(IllegalArgumentException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new StatusResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
            )
        );
    }
}
