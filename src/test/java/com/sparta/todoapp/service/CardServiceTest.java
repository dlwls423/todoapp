package com.sparta.todoapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.sparta.todoapp.controller.exception.AuthorizeException;
import com.sparta.todoapp.controller.exception.BadAccessToCardException;
import com.sparta.todoapp.dto.CardInListResponseDto;
import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
import com.sparta.todoapp.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    CardRepository cardRepository;

    @Mock
    UserRepository userRepository;

    CardService cardService;

    User user1;
    User user2;
    Card card;
    Card privateCard;

    @BeforeEach
    void setup() {
        // given
        cardService = new CardService(cardRepository, userRepository);
        user1 = new User("user1", "1234");
        user2 = new User("user2", "1234");
        card = new Card("제목", "내용", false, false, user1);
        privateCard = new Card("제목", "내용", false, true, user1);
    }

    @Nested
    @DisplayName("반복 사용되는 매서드")
    class Test1 {
        @Test
        @DisplayName("카드 id로 카드 찾기")
        void test1() {
            // given
            Long cardId = 1L;
            card.setCardId(cardId);
            given(cardRepository.findById(cardId)).willReturn(Optional.of(card));

            // when
            Card findCard = cardService.getCardEntity(cardId);

            // then
            assertEquals(card, findCard);
        }

        @Test
        @DisplayName("카드 id로 카드 찾기 - 완료된 카드")
        void test2() {
            // given
            Long cardId = 1L;
            card.setCardId(cardId);
            card.setComplete(true);
            given(cardRepository.findById(cardId)).willReturn(Optional.of(card));

            // when
            BadAccessToCardException exception = assertThrows(BadAccessToCardException.class, () ->
                cardService.getCardEntity(cardId)
            );

            // then
            assertEquals("완료된 카드입니다.", exception.getMessage());
        }

        @Test
        @DisplayName("카드 권한 없음")
        void test3() {
            //when
            AuthorizeException exception = assertThrows(AuthorizeException.class, () ->
                cardService.checkUser(card, user2)
            );

            //then
            assertEquals("작성자만 삭제/수정할 수 있습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("비공개된 카드 - 타인이 접근")
        void test4() {
            //when
            BadAccessToCardException exception = assertThrows(BadAccessToCardException.class, () ->
                cardService.checkPrivateCardAuthority(privateCard, user2)
            );

            //then
            assertEquals("비공개된 카드입니다.", exception.getMessage());
        }

        @Test
        @DisplayName("비공개된 카드 - 작성자가 접근")
        void test5() {
            //when
            boolean hasNoAuthority = cardService.checkPrivateCardAndUser(privateCard, user1);

            //then
            assertFalse(hasNoAuthority);
        }
    }


    @Nested
    @DisplayName("기능별 테스트")
    class Test2 {
        @Test
        @DisplayName("카드 생성")
        void test6() {
            // given
            CardRequestDto cardRequestDto = new CardRequestDto("카드 제목", "카드 내용", false);
            card.setCardId(1L);
            CardResponseDto cardResponseDto = new CardResponseDto(card);
            when(cardRepository.save(any(Card.class))).thenReturn(card);

            // when
            CardResponseDto responseDto = cardService.createCard(cardRequestDto, user1);

            // then
            assertEquals(cardResponseDto.getCardId(), responseDto.getCardId());
        }

        @Test
        @DisplayName("카드 단일 조회")
        void test7() {
            // given
            Long cardId = 1L;
            CardRequestDto requestDto = new CardRequestDto("제목", "내용", false);
            given(cardRepository.findById(cardId)).willReturn(Optional.of(card));

            // when
            CardResponseDto responseDto = cardService.getCard(cardId, user1);

            // then
            assertEquals(card.getCardId(), responseDto.getCardId());
        }

        @Test
        @DisplayName("카드 전체 조회")
        void test8() {
            // given
            Card card1 = new Card("카드 제목", "카드 내용", false, false, user1);
            Card card2 = new Card("카드 제목", "카드 내용", false, true, user1);
            Card card3 = new Card("카드 제목", "카드 내용", false, false, user2);
            Card card4 = new Card("카드 제목", "카드 내용", false, true, user2);

            List<User> userList = new ArrayList<>();
            userList.add(user1);
            userList.add(user2);

            List<Card> cardList1 = new ArrayList<>();
            cardList1.add(card1);
            cardList1.add(card2);

            List<Card> cardList2 = new ArrayList<>();
            cardList2.add(card3);
            cardList2.add(card4);

            given(userRepository.findAll()).willReturn(userList);
            given(cardRepository.findAllByUserAndCompleteFalseOrderByCreatedAtDesc(user1)).willReturn(cardList1);
            given(cardRepository.findAllByUserAndCompleteFalseOrderByCreatedAtDesc(user2)).willReturn(cardList2);

            // when
            Map<String, List<CardInListResponseDto>> map = cardService.getCards(user1);

            // then
            assertEquals(2, map.get("user1").size());
            assertEquals(1, map.get("user2").size());
        }

        @Test
        @DisplayName("카드 수정")
        void test9() {
            // given
            Long cardId = 1L;
            CardRequestDto requestDto = new CardRequestDto("수정 제목", "수정 내용", false);
            card.setCardId(cardId);

            given(cardRepository.findById(cardId)).willReturn(Optional.of(card));

            // when
            CardResponseDto responseDto = cardService.updateCard(cardId, requestDto, user1);

            // then
            System.out.println("Title: " + responseDto.getTitle());
            assertEquals(requestDto.getTitle(), responseDto.getTitle());
        }

        @Test
        @DisplayName("카드 완료 설정")
        void test10() {
            // given
            Long cardId = 1L;

            given(cardRepository.findById(cardId)).willReturn(Optional.of(card));

            // when
            cardService.completeCard(cardId, user1);

            // then
            assertTrue(card.isComplete());
        }

        @Test
        @DisplayName("카드 검색 조회")
        void test11() {
            // given
            Card card1 = new Card("제목", "내용", false, false, user1);
            Card card2 = new Card("제목", "내용", false, false, user1);
            Card card3 = new Card("제목", "내용", false, true, user1);

            List<Card> cardList = new ArrayList<>();
            cardList.add(card1);
            cardList.add(card2);
            cardList.add(card3);

            given(cardRepository.findAllByTitleContainsAndCompleteFalseOrderByCreatedAtDesc("제목")).willReturn(cardList);

            // when
            List<CardInListResponseDto> responseDtoList = cardService.getCardsMatchSearchWord("제목", user1);

            // then
            assertEquals(3, responseDtoList.size());
        }
    }

}