package com.sparta.todoapp.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.todoapp.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("UserRepository 테스트")
    void test1() {
        //given
        User user = new User("yejin", "password");

        //when
        userRepository.save(user);
        User saveUser = userRepository.findByUsername("yejin").orElse(null);

        //then
        assertEquals(saveUser, user);
    }
}