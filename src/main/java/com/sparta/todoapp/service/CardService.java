package com.sparta.todoapp.service;

import com.sparta.todoapp.controller.exception.AuthorizeException;
import com.sparta.todoapp.controller.exception.CardNotFoundException;
import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.CardInListResponseDto;
import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
import com.sparta.todoapp.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    private final UserRepository userRepository;

    public CardResponseDto createCard(CardRequestDto cardRequestDto, User user) {
        Card card = new Card(cardRequestDto, user);
        Card saveCard = cardRepository.save(card);
        return new CardResponseDto(saveCard);
    }

    public CardResponseDto getCard(Long cardId) {
        Card card = getCardEntity(cardId);
        CardResponseDto responseDto = new CardResponseDto(card);
        return responseDto;
    }

    public Map<String,List<CardInListResponseDto>> getCards() {
        Map<String, List<CardInListResponseDto>> usernameCardMap = new HashMap<>();

        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            List<CardInListResponseDto> cardList = cardRepository.findAllByUserOrderByCreatedAtDesc(user)
                .stream().map(CardInListResponseDto::new).collect(Collectors.toList());
            usernameCardMap.put(user.getUsername(), cardList);
        }

        return usernameCardMap;
    }

    @Transactional
    public CardResponseDto updateCard(Long cardId, CardRequestDto cardRequestDto, User user) {
        Card card = getCardEntity(cardId);
        checkUser(card, user);
        card.update(cardRequestDto);
        return new CardResponseDto(card);
    }

    private Card getCardEntity(Long cardId){
        Card card = cardRepository.findById(cardId).orElseThrow(
            () -> new CardNotFoundException("해당 카드를 찾을 수 없습니다.")
        );
        return card;
    }

    private void checkUser(Card card, User user){
        if(!card.getUser().getUsername().equals(user.getUsername())){
            throw new AuthorizeException("권한이 없습니다.");
        }
    }
}
