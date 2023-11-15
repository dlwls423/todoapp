package com.sparta.todoapp.controller;

import com.sparta.todoapp.controller.exception.AuthorizeException;
import com.sparta.todoapp.controller.exception.EntityNotFoundException;
import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.CardInListResponseDto;
import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.dto.StatusResponseDto;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.CardService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(
        @RequestBody CardRequestDto cardRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        CardResponseDto responseDto = cardService.createCard(cardRequestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> getCard(@PathVariable Long cardId){
        CardResponseDto responseDto = cardService.getCard(cardId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<Map<String, List<CardInListResponseDto>>> getCards(){
        Map<String, List<CardInListResponseDto>> usernameCardMap = cardService.getCards();

        return ResponseEntity.ok(usernameCardMap);
    }

    @PutMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long cardId, @RequestBody CardRequestDto cardRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        CardResponseDto responseDto = cardService.updateCard(cardId, cardRequestDto, userDetails.getUser());
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{cardId}")
    public ResponseEntity<StatusResponseDto> completeCard(@PathVariable Long cardId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        cardService.completeCard(cardId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(
            new StatusResponseDto(
                HttpStatus.OK.value(),
                "할일카드 완료"
            )
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StatusResponseDto> CardNotFoundExceptionHandler(EntityNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new StatusResponseDto(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
            )
        );
    }

    @ExceptionHandler(AuthorizeException.class)
    public ResponseEntity<StatusResponseDto> AuthorizeExceptionHandler(AuthorizeException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            new StatusResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage()
            )
        );
    }

}
