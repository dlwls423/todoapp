package com.sparta.todoapp.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sparta.todoapp.controller.exception.BadAccessToCardException;
import com.sparta.todoapp.dto.CardInListResponseDto;
import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.UserRepository;
import com.sparta.todoapp.service.CardService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CardServiceIntegrationTest {

    @Autowired
    CardService cardService;

    @Autowired
    UserRepository userRepository;

    User user1;

    CardResponseDto createdCard;


    @Test
    @Order(1)
    @DisplayName("카드 생성")
    void test1() {
        // given
        CardRequestDto requestDto = new CardRequestDto("제목", "내용", false);

        user1 = userRepository.findById(1L).orElse(null);

        // when
        CardResponseDto responseDto = cardService.createCard(requestDto, user1);

        // then
        assertNotNull(responseDto.getCardId());
        assertEquals(requestDto.getTitle(), responseDto.getTitle());
        createdCard = responseDto;
    }

    @Test
    @Order(2)
    @DisplayName("카드 단일 조회")
    void test2() {
        // given
        Long cardId = this.createdCard.getCardId();

        // when
        CardResponseDto responseDto = cardService.getCard(cardId, user1);

        // then
        assertEquals(this.createdCard.getTitle(), responseDto.getTitle());
        assertEquals(this.createdCard.getUsername(), responseDto.getUsername());
    }

    @Test
    @Order(3)
    @DisplayName("카드 전체 조회")
    void test3() {
        // given
        // when
        Map<String,List<CardInListResponseDto>> cardMap = cardService.getCards(user1);
        Long createdCardId = this.createdCard.getCardId();
        List<CardInListResponseDto> foundCardList = cardMap.get(user1.getUsername());
        CardInListResponseDto responseDto = foundCardList.stream()
            .filter(CardInListResponseDto -> CardInListResponseDto.getCardId().equals(createdCardId))
            .findFirst()
            .orElse(null);


        // then
        assertNotNull(responseDto);
        assertEquals(this.createdCard.getTitle(), responseDto.getTitle());
        assertEquals(this.createdCard.getUsername(), responseDto.getUsername());
    }

    @Test
    @Order(4)
    @DisplayName("카드 수정")
    void test4() {
        // given
        Long cardId = this.createdCard.getCardId();
        CardRequestDto requestDto = new CardRequestDto("제목 수정", "내용 수정", false);

        // when
        CardResponseDto responseDto = cardService.updateCard(cardId, requestDto, user1);

        // then
        assertEquals(this.createdCard.getCardId(), responseDto.getCardId());
        assertEquals(requestDto.getTitle(), responseDto.getTitle());
    }

    @Test
    @Order(5)
    @DisplayName("카드 완료")
    void test5() {
        // given
        Long cardId = this.createdCard.getCardId();

        // when
        cardService.completeCard(cardId, user1);
        BadAccessToCardException exception = assertThrows(BadAccessToCardException.class, () ->
            cardService.getCard(cardId, user1)
        );

        // then
        assertEquals("완료된 카드입니다.", exception.getMessage());
    }

}
