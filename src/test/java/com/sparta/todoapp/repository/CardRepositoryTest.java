package com.sparta.todoapp.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.todoapp.config.JpaConfig;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@Import(JpaConfig.class )
class CardRepositoryTest {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    UserRepository userRepository;

    User user1;
    User user2;

    Card card1;
    Card card2;
    Card card3;


    @BeforeEach
    void setup() {
        // given
        user1 = new User("사용자1", "abcd");
        user2 = new User("사용자2", "abcd");
        userRepository.save(user1);
        userRepository.save(user2);
        card1 = new Card("코딩 연습하기", "내용", false, false, user1);
        card2 = new Card("조깅하기", "내용", false, false, user1);
        card3 = new Card("코딩 테스트 문제 풀기", "내용", false, false, user2);
        cardRepository.save(card1);
        cardRepository.save(card2);
        cardRepository.save(card3);
    }

    @Test
    @DisplayName("사용자별 카드 조회 테스트")
    void test1(){
        //when
        List<Card> cardList = cardRepository.findAllByUserAndCompleteFalseOrderByCreatedAtDesc(this.user1);

        //then
        for (Card card : cardList) {
            System.out.println("카드 제목: " + card.getTitle());
        }
        assertEquals(card2, cardList.get(0));
    }

    @Test
    @DisplayName("검색어별 카드 조회 테스트")
    void test2(){
        //when
        List<Card> cardList = cardRepository.findAllByTitleContainsAndCompleteFalseOrderByCreatedAtDesc("코딩");

        //then
        for (Card card : cardList) {
            System.out.println("카드 제목: " + card.getTitle());
        }
        assertEquals(cardList.get(0), card3);
    }
}