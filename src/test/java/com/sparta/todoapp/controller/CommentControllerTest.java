package com.sparta.todoapp.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.config.WebSecurityConfig;
import com.sparta.todoapp.dto.CardInListResponseDto;
import com.sparta.todoapp.dto.CardRequestDto;
import com.sparta.todoapp.dto.CardResponseDto;
import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.entity.Card;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.CommentService;
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
    controllers = {CommentController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )
    }
)
public class CommentControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

    static User testUser;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity(new MockSpringSecurityFilter()))
            .build();
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
    @DisplayName("댓글 생성")
    void test1() throws Exception {
        //given
        mockUserSetup();
        Card card = new Card("제목", "내용", false, false, testUser);
        CommentRequestDto requestDto = new CommentRequestDto("댓글 내용");
        Comment comment = new Comment(requestDto, testUser, card);
        CommentResponseDto responseDto = new CommentResponseDto(comment);

        given(commentService.createComment(1L, requestDto, testUser)).willReturn(responseDto);

        //when - then
        mvc.perform(post("/api/cards/{cardId}/comments", 1L)
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
            )
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("카드별 댓글 조회")
    void test2() throws Exception {
        //given
        mockUserSetup();
        Card card = new Card("제목", "내용", false, false, testUser);
        Comment comment = new Comment("댓글 내용", testUser, card);
        CommentResponseDto responseDto = new CommentResponseDto(comment);
        List<CommentResponseDto> responseDtoList = new ArrayList<>();
        responseDtoList.add(responseDto);

        given(commentService.getComments(1L)).willReturn(responseDtoList);

        //when - then
        mvc.perform(get("/api/cards/{cardId}/comments", 1L)
                .principal(mockPrincipal)
            )
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("카드 수정")
    void test3() throws Exception {
        //given
        mockUserSetup();
        Card card = new Card("제목", "내용", false, false, testUser);
        CommentRequestDto requestDto = new CommentRequestDto("댓글 내용");
        Comment comment = new Comment(requestDto, testUser, card);
        CommentResponseDto responseDto = new CommentResponseDto(comment);

        given(commentService.updateComment(1L, 1L, requestDto, testUser)).willReturn(responseDto);

        //when - then
        mvc.perform(patch("/api/cards/{cardId}/comments/{commentId}", 1L , 1L)
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
            )
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("카드 삭제")
    void test4() throws Exception {
        //given
        mockUserSetup();

        //when - then
        mvc.perform(delete("/api/cards/{cardId}/comments/{commentId}", 1L, 1L)
                .principal(mockPrincipal)
            )
            .andExpect(status().is2xxSuccessful());
    }

}
