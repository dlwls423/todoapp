package com.sparta.todoapp.controller;


import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.config.WebSecurityConfig;
import com.sparta.todoapp.dto.CardInListResponseDto;
import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.CardService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(
    controllers = {CardController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )
    }
)
public class CardControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CardService cardService;

    static User testUser;

    CardRequestDto requestDto;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity(new MockSpringSecurityFilter()))
            .build();
        requestDto = new CardRequestDto("카드 제목", "카드 내용", false);
    }

    private void mockUserSetup() {
        String username = "leeyejin";
        String password = "11111111";
        testUser = new User(username, password);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "",
            testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("카드 생성")
    void test1() throws Exception {
        //given
        mockUserSetup();

        //when - then
        mvc.perform(post("/api/cards")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
            )
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("카드 단일 조회")
    void test2() throws Exception {
        //given
        mockUserSetup();
        CardResponseDto responseDto = new CardResponseDto(new Card(requestDto, testUser));
        given(cardService.getCard(1L, testUser)).willReturn(responseDto);

        //when - then
        mvc.perform(get("/api/cards/{cardId}", 1L)
                .principal(mockPrincipal)
            )
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("카드 전체 조회")
    void test3() throws Exception {
        //given
        mockUserSetup();
        Card card = new Card("제목", "내용", false, false, testUser);
        Map<String, List<CardInListResponseDto>> responseDtoMap = new HashMap<>();
        responseDtoMap.put(testUser.getUsername(), new ArrayList<>());
        CardInListResponseDto responseDto = new CardInListResponseDto(card);
        responseDtoMap.get(testUser.getUsername()).add(responseDto);
        responseDtoMap.get(testUser.getUsername()).add(responseDto);
        responseDtoMap.get(testUser.getUsername()).add(responseDto);

        given(cardService.getCards(testUser)).willReturn(responseDtoMap);

        //when - then
        mvc.perform(get("/api/cards" )
                .principal(mockPrincipal)
            )
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("카드 수정")
    void test4() throws Exception {
        //given
        mockUserSetup();

        //when - then
        mvc.perform(put("/api/cards/{cardId}", 1L )
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
            )
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("카드 완료 체크")
    void test5() throws Exception {
        //given
        mockUserSetup();

        //when - then
        mvc.perform(patch("/api/cards/{cardId}", 1L )
                .principal(mockPrincipal)
            )
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("카드 제목 검색")
    void test6() throws Exception {
        //given
        mockUserSetup();
        List<CardInListResponseDto> responseDtoList = new ArrayList<>();
        CardInListResponseDto responseDto = new CardInListResponseDto(new Card("제목", "내용", false, false, testUser));
        responseDtoList.add(responseDto);
        given(cardService.getCardsMatchSearchWord("제목", testUser)).willReturn(responseDtoList);

        //when - then
        mvc.perform(get("/api/cards/search")
                .param("title", "제목")
                .principal(mockPrincipal)
            )
            .andExpect(status().is2xxSuccessful());
    }

}