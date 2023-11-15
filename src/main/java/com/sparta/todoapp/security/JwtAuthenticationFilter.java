package com.sparta.todoapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.dto.StatusResponseDto;
import com.sparta.todoapp.dto.UserRequestDto;
import com.sparta.todoapp.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), UserRequestDto.class);

            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                    requestDto.getUsername(),
                    requestDto.getPassword(),
                    null
                )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {

        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        String token = jwtUtil.createToken(username);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        insertStatusInfoIntoResponse(response, HttpStatus.OK.value(), "로그인 성공");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        insertStatusInfoIntoResponse(response, HttpStatus.BAD_REQUEST.value(), "회원을 찾을 수 없습니다.");
    }

    public void insertStatusInfoIntoResponse(HttpServletResponse response, int statusCode, String msg) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();

        StatusResponseDto statusResponseDto = new StatusResponseDto(statusCode, msg);
        String result = objectMapper.writeValueAsString(statusResponseDto);

        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result);
    }

}