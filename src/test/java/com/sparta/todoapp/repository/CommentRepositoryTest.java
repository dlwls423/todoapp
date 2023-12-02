package com.sparta.todoapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CardRepository cardRepository;

    @Test
    @DisplayName("CommentRepository 테스트")
    void test1(){
        //given
        User user = new User("사용자이름", "abcd");
        userRepository.save(user);
        Card card = new Card("제목", "내용", false, false, user);
        cardRepository.save(card);
        Comment comment1 = new Comment("댓글 내용1", user, card);
        Comment comment2 = new Comment("댓글 내용2", user, card);
        Comment comment3 = new Comment("댓글 내용3", user, card);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        //when
        List<Comment> commentList = commentRepository.findAllByCard(card);

        //then
        assertEquals(commentList.size(), 3);
    }
}