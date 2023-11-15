package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByCard(Card card);
}
