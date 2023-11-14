package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CardRepository;
import com.sparta.todoapp.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    private final UserRepository userRepository;

    public Map<String,List<CardResponseDto>> getCards() {
        Map<String, List<CardResponseDto>> usernameCardMap = new HashMap<>();

        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            List<CardResponseDto> cardList = cardRepository.findAllByUserOrderByCreatedAtDesc(user)
                .stream().map(CardResponseDto::new).collect(Collectors.toList());
            usernameCardMap.put(user.getUsername(), cardList);
        }

        return usernameCardMap;
    }
}
