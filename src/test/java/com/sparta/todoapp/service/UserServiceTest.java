package com.sparta.todoapp.service;

import static org.hamcrest.Matchers.any;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    //테스트 클래스에 선언
    @Captor
    ArgumentCaptor<User> argumentCaptor;


    @Test
    @DisplayName("회원가입 - 중복 회원 존재")
    void test1() {
        // given
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

    @Test
    @DisplayName("회원가입 ")
    void test2() {
        // given
        UserRequestDto userRequestDto = new UserRequestDto("lucy", "11111111");
        User user = new User("lucy", "encodedPassword");

        given(userRepository.findByUsername("lucy")).willReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        userService.signup(userRequestDto);


        // then
        verify(passwordEncoder).encode("11111111");
        verify(userRepository).findByUsername("lucy");
        verify(userRepository).save(any(User.class));

        verify(userRepository).save(argumentCaptor.capture());
        assertEquals(user.getUsername(), argumentCaptor.getValue().getUsername());
    }
}