package com.depth.planet.domain.auth.dto;

import com.depth.planet.domain.user.dto.UserDto;
import com.depth.planet.domain.user.entity.User;
import com.depth.planet.domain.user.entity.enums.AgeGroup;
import com.depth.planet.domain.user.entity.enums.GenderType;
import com.depth.planet.domain.user.entity.enums.MBTI;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

public class AuthDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(description = "로그인 요청 DTO")
    public static class SignInRequest {
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @Schema(description = "사용자 이메일", example = "test@example.com")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Schema(description = "사용자 비밀번호", example = "password123")
        private String password;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(description = "로그인 응답 DTO")
    public static class SignInResponse {
        @Schema(description = "발급된 Access Token")
        private String accessToken;
        @Schema(description = "Access Token 만료 시간")
        private LocalDateTime accessTokenExpiresAt;
        @Schema(description = "로그인한 사용자 정보")
        private UserDto.UserResponse user;

        public static SignInResponse of(
                String accessToken,
                UserDto.UserResponse user,
                LocalDateTime accessTokenExpiresAt
        ) {
            return SignInResponse.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiresAt(accessTokenExpiresAt)
                    .user(user)
                    .build();
        }
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @Schema(description = "회원가입 요청 DTO")
    public static class SignUpRequest {
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @Schema(description = "사용자 이메일", example = "test@example.com")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Schema(description = "사용자 비밀번호", example = "password123")
        private String password;

        @NotBlank(message = "이름을 입력해주세요.")
        @Schema(description = "사용자 이름", example = "홍길동")
        private String name;

        @Schema(description = "사용자 MBTI 유형", example = "ENTJ")
        private MBTI mbti;

        @Schema(description = "사용자 성별", example = "Male")
        private GenderType gender;

        @Schema(description = "사용자 취미 목록", example = "[\"등산\", \"독서\"]")
        private List<String> hobbies;

        @Schema(description = "사용자 연령대", example = "Teenager")
        private AgeGroup ageGroup;

        public User toEntity(PasswordEncoder passwordEncoder) {
            return User.builder()
                    .email(email)
                    .name(name)
                    .mbti(mbti)
                    .gender(gender)
                    .ageGroup(ageGroup)
                    .hobbies(hobbies)
                    .password(passwordEncoder.encode(password))
                    .build();
        }
    }

}
