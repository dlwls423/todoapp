package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.UserRequestDto;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void signup(UserRequestDto requestDto) {
        User user = new User(requestDto);
        userRepository.save(user);
    }
}
