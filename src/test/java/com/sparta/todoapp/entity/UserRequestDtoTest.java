package com.sparta.todoapp.entity;

import static org.assertj.core.api.Assertions.*;

import com.sparta.todoapp.dto.UserRequestDto;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserRequestDtoTest {

    @DisplayName("dto 생성")
    @Nested
    class createUserRequestDto {

        @DisplayName("성공")
        @Test
        void createUserRequestDto_success() {
            // given
            UserRequestDto requestDto = new UserRequestDto.mockBuilder().username("username").password("password").build();

            // when
            Set<ConstraintViolation<UserRequestDto>> violations = validate(requestDto);

            // then
            assertThat(violations).isEmpty();
        }

        @DisplayName("실패 - 잘못된 username")
        @Test
        void createUserRequestDto_wrongUsername(){
            // given
            UserRequestDto requestDto = new UserRequestDto("user name", "password");

            // when
            Set<ConstraintViolation<UserRequestDto>> violations = validate(requestDto);

            // then
            System.out.println(violations);
            assertThat(violations).hasSize(1);
            assertThat(violations)
                .extracting("message")
                .contains("must match \"^[a-z0-9]*$\"");
        }

        @DisplayName("실패 - 잘못된 password")
        @Test
        void createUserRequestDto_wrongPassword(){
            // given
            UserRequestDto requestDto = new UserRequestDto("username", "wrong**password");

            // when
            Set<ConstraintViolation<UserRequestDto>> violations = validate(requestDto);

            // then
            assertThat(violations).hasSize(1);
            assertThat(violations)
                .extracting("message")
                .contains("must match \"^[a-zA-Z0-9]*$\"");
        }
    }

    private Set<ConstraintViolation<UserRequestDto>> validate(UserRequestDto userRequestDTO) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(userRequestDTO);
    }

}
