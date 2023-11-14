package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.service.CardService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;


    @GetMapping
    public ResponseEntity<Map<String, List<CardResponseDto>>> getCards(){
        Map<String, List<CardResponseDto>> usernameCardMap= cardService.getCards();

        return ResponseEntity.ok(usernameCardMap);
    }

}
