package com.depth.planet.domain.user.dto;

import java.util.List;

import com.depth.planet.domain.user.entity.User;
import com.depth.planet.domain.user.entity.enums.AgeGroup;
import com.depth.planet.domain.user.entity.enums.GenderType;
import com.depth.planet.domain.user.entity.enums.MBTI;

import com.depth.planet.domain.user.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @Schema(description = "회원 정보 수정 요청 DTO")
    public static class UserUpdateRequest {
        @Schema(description = "수정할 비밀번호", example = "newPassword123")
        private String password;

        @Schema(description = "수정할 이름", example = "김철수")
        private String name;

        @Schema(description = "수정할 MBTI", example = "INFP")
        private MBTI mbti;

        @Schema(description = "수정할 성별", example = "Male")
        private GenderType gender;

        @Schema(description = "수정할 취미 목록", example = "[\"영화감상\", \"게임\"]")
        private List<String> hobbies;

        @Schema(description = "수정할 연령대", example = "Adult")
        private AgeGroup ageGroup;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @Schema(description = "회원 정보 응답 DTO")
    public static class UserResponse {
        @Schema(description = "사용자 이메일", example = "test@example.com")
        private String email;

        @Schema(description = "사용자 이름", example = "홍길동")
        private String name;

        @Schema(description = "사용자 권한", example = "User, Admin")
        private Role role;

        @Schema(description = "사용자 MBTI", example = "ENTJ")
        private MBTI mbti;

        @Schema(description = "사용자 성별", example = "Male")
        private GenderType gender;

        @Schema(description = "사용자 취미 목록", example = "[\"등산\", \"독서\"]")
        private List<String> hobbies;

        @Schema(description = "사용자 연령대", example = "Teenager")
        private AgeGroup ageGroup;

        public static UserResponse from(User user) {
            return UserResponse.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .mbti(user.getMbti())
                    .gender(user.getGender())
                    .role(user.getRole())
                    .hobbies(user.getHobbies())
                    .build();
        }
    }
}
