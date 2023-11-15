package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllByUserAndCompleteFalseOrderByCreatedAtDesc(User user);

    List<Card> findAllByTitleContainsAndCompleteFalseOrderByCreatedAtDesc(String searchWord);
}
