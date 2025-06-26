package com.depth.planet.domain.user.dto;

import java.util.List;

import com.depth.planet.domain.user.entity.User;
import com.depth.planet.domain.user.entity.enums.GenderType;
import com.depth.planet.domain.user.entity.enums.MBTI;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class UserUpdateRequest {
        private String password;

        private String name;

        private MBTI mbti;

        private GenderType gender;

        private List<String> hobbies;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class UserResponse {
        private String email;

        private String name;

        private MBTI mbti;

        private GenderType gender;

        private List<String> hobbies;

        public static UserResponse from(User user) {
            return UserResponse.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .mbti(user.getMbti())
                    .gender(user.getGender())
                    .hobbies(user.getHobbies())
                    .build();
        }
    }
}
