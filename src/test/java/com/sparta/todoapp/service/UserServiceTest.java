package com.sparta.todoapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.sparta.todoapp.dto.UserRequestDto;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 - 중복 회원 존재")
    void test1() {
        // given
        UserService userService = new UserService(userRepository, passwordEncoder);
        UserRequestDto userRequestDto = new UserRequestDto("lucy", "11111111");
        User user = new User("lucy", "encodedPassword");

        given(userRepository.findByUsername("lucy")).willReturn(Optional.of(user));

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.signup(userRequestDto);
        });
        
        // then
        assertEquals("중복된 사용자가 존재합니다.", exception.getMessage());
    }
}