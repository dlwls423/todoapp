package com.sparta.todoapp.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CardTest {
    Card card;

    @BeforeEach()
    void setUp() {
        card = new Card();
    }
    @Test
    @DisplayName("setComplete 테스트")
    void test1() {
        card.setComplete(true);
        assertTrue(card.isComplete());
    }

}