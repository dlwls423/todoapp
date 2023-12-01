package com.sparta.todoapp.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentTest {
    Comment comment;

    @BeforeEach()
    void setUp() {
        comment = new Comment();
    }

    @Test
    @DisplayName("updateContent 테스트")
    void test1() {
        comment.updateContent("안녕하세요");
        assertEquals("안녕하세요", comment.getContent());
    }
}