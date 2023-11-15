package com.sparta.todoapp.service;

import com.sparta.todoapp.controller.exception.BadAccessToCardException;
import com.sparta.todoapp.controller.exception.AuthorizeException;
import com.sparta.todoapp.controller.exception.EntityNotFoundException;
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

    public CardResponseDto getCard(Long cardId, User user) {
        Card card = getCardEntity(cardId);
        checkPrivateCardAuthority(card, user);
        CardResponseDto responseDto = new CardResponseDto(card);
        return responseDto;
    }

    public Map<String,List<CardInListResponseDto>> getCards(User inputUser) {
        Map<String, List<CardInListResponseDto>> usernameCardMap = new HashMap<>();

        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            List<Card> cardList = cardRepository.findAllByUserAndCompleteFalseOrderByCreatedAtDesc(user);
            cardList.removeIf(card -> checkPrivateCardAndUser(card, inputUser));
            List<CardInListResponseDto> cardResponseDtoList = cardList.stream().map(CardInListResponseDto::new).toList();
            usernameCardMap.put(user.getUsername(), cardResponseDtoList);
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

    @Transactional
    public void completeCard(Long cardId, User user) {
        Card card = getCardEntity(cardId);
        checkUser(card, user);
        card.setComplete(true);
    }

    public void checkPrivateCardAuthority(Card card, User user){
        if(checkPrivateCardAndUser(card, user))
            throw new BadAccessToCardException("비공개된 카드입니다.");
    }

    public boolean checkPrivateCardAndUser(Card card, User user){
        return card.isPrivateCard() && !card.getUser().getUsername().equals(user.getUsername());
    }

    public Card getCardEntity(Long cardId){
        Card card = cardRepository.findById(cardId).orElseThrow(
            () -> new EntityNotFoundException("해당 카드를 찾을 수 없습니다.")
        );
        if(card.isComplete()) throw new BadAccessToCardException("완료된 카드입니다.");
        return card;
    }

    public void checkUser(Card card, User user){
        if(!card.getUser().getUsername().equals(user.getUsername())){
            throw new AuthorizeException("작성자만 삭제/수정할 수 있습니다.");
        }
    }
}
